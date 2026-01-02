import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { MatDividerModule } from "@angular/material/divider";
import { MatButtonModule } from '@angular/material/button';
import { SeatSelectionComponent } from './seat-selection/seat-selection.component';
import { TicketFormComponent } from './ticket-form/ticket-form.component';

@Component({
  selector: 'app-booking',
  imports: [
    MatIconModule,
    CommonModule,
    MatStepperModule,
    MatDividerModule,
    MatButtonModule,
    SeatSelectionComponent,
    TicketFormComponent
],
  templateUrl: './booking.component.html',
  styleUrl: './booking.component.css'
})
export class BookingComponent implements OnInit{

  flightUUID!: string;
  selectedSeats = new Set<string>();
  
  @ViewChild('stepper') stepper!: MatStepper;

  constructor(
    private readonly route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.flightUUID = this.route.snapshot.paramMap.get('flightUUID') ?? '';
  }
  
  previous(): void {
    this.stepper.previous();
  }

  next(): void {
    this.stepper.next();
  }

  hasSelectedSeats(): boolean {
    return this.selectedSeats.size > 0;
  }
}
