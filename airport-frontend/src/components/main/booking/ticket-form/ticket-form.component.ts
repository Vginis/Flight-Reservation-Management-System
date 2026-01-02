import { CommonModule } from '@angular/common';
import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CommonUtils } from '../../../../utils/common.util';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { LuggagesModalComponent } from './luggages-modal/luggages-modal.component';
import { FlightSeatLayoutService } from '../../../../services/backend/flightseatlayout.service';
import { SeatState } from '../../../../models/flight.models';

@Component({
  selector: 'app-ticket-form',
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatButtonModule
  ],
  templateUrl: './ticket-form.component.html',
  styleUrl: './ticket-form.component.css'
})
export class TicketFormComponent implements OnInit, OnChanges {

  @Input() selectedSeats!: Set<string>;
  @Input() flightUUID!: string;
  reservationForm: FormGroup;

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly dialog: MatDialog,
    private readonly flightSeatLayoutService: FlightSeatLayoutService
  ) {
    this.reservationForm = this.formBuilder.group({
      tickets: this.formBuilder.array([])
    })
  }

  ngOnInit(): void {
    this.patchForm();    
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedSeats']) {
      this.patchForm();
    }
  }

  private patchForm(): void {
    if(!this.selectedSeats) return;

    this.tickets.clear();
    this.selectedSeats.forEach((seat) => {
      const row = seat.split("-")[0];
      const column = seat.split("-")[1];
      this.tickets.push(this.formBuilder.group({
        row: [Number(row), Validators.required],
        column: [Number(column), Validators.required],
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        passport: ['', Validators.required],
        luggages: this.formBuilder.array([])
      }));
    });
  }

  completeReservation(): void {

  }

  removeTicket(index: number): void {
    const ticketToRemove = this.tickets.at(index).value;
    const seatId = `${ticketToRemove.row}-${ticketToRemove.column}`;
    const payload = {
        flightUUID: this.flightUUID,
        rowIndex: ticketToRemove.row,
        columnIndex: ticketToRemove.column,
        seatReservationState: 'AVAILABLE' as SeatState
    }
    this.flightSeatLayoutService.deselectSeat(seatId, payload);
    this.tickets.removeAt(index);
  }

  openLuggagesModal(ticketIndex: number): void {
    const dialogRef = this.dialog.open(LuggagesModalComponent);
    dialogRef.afterClosed().subscribe((result: { value: number }[] | undefined) => {
      if (result) {
        const luggageFormArray = this.getLuggages(ticketIndex);
        luggageFormArray.clear();
        result.forEach(bag => {
          luggageFormArray.push(this.formBuilder.group({
            weight: [bag.value, Validators.required]
          }));
        });
      }
    });
  }

  getSeatLabel(row: number, column: number): string {
    const seatLetter = CommonUtils.seatLetters[column - 1] ?? '?';
    return `${row}${seatLetter}`;
  }

  getLuggages(ticketIndex: number): FormArray {
    return this.tickets.at(ticketIndex).get('luggages') as FormArray;
  }

  get tickets(): FormArray {
    return this.reservationForm.get('tickets') as FormArray;
  }
}
