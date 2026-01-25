import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { FlightRepresentation } from '../../../../../models/flight.models';
import { FlightService } from '../../../../../services/backend/flight.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs';
import { AirportService } from '../../../../../services/backend/airport.service';
import { MatNativeDateModule } from '@angular/material/core';
import { AircraftService } from '../../../../../services/backend/aircraft.service';
import { CommonUtils } from '../../../../../utils/common.util';

@Component({
  selector: 'app-flights-create-modal',
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    ReactiveFormsModule,
    MatIconModule,
    MatSelectModule,
    MatAutocompleteModule,
    MatIconModule
  ],
  templateUrl: './flights-create-modal.component.html',
  styleUrl: './flights-create-modal.component.css'
})
export class FlightsCreateModalComponent implements OnInit {
  flightCreateForm!: FormGroup;
  isEditMode: boolean = false;
  aircrafts: any = [];
  airports: any = [];
  defaultStartDate!: Date;

  constructor(
    @Inject(MAT_DIALOG_DATA) public readonly flight: FlightRepresentation,
    private readonly formBuilder: FormBuilder,
    private readonly dialogRef: MatDialogRef<FlightsCreateModalComponent>,
    private readonly flightService: FlightService,
    private readonly airportService: AirportService,
    private readonly aircraftService: AircraftService,
    private readonly snackbar: SnackbarService
  ) {
    this.flightCreateForm = this.formBuilder.group({
      flightNumber: ['', Validators.required],
      departureAirport: ['', Validators.required],
      departureDate: ['', Validators.required],
      departureTime: ['', Validators.required],
      arrivalAirport: ['', Validators.required],
      arrivalDate: ['', Validators.required],
      arrivalTime: ['', Validators.required],
      aircraft: ['', Validators.required]
    },
    { validators: [this.differentAirportsValidator]});
  }

  ngOnInit(): void {
    if(this.flight){
      this.isEditMode = true;
      this.flightCreateForm = this.formBuilder.group({
        flightNumber: [{ disabled: true, value: ''}, Validators.required],
        departureAirport: [{ disabled: true, value: ''}, Validators.required],
        departureDate: ['', Validators.required],
        departureTime: ['', Validators.required],
        arrivalAirport: [{ disabled: true, value: ''}, Validators.required],
        arrivalDate: ['', Validators.required],
        arrivalTime: ['', Validators.required],
        aircraft: [{ disabled: true, value: ''}, Validators.required]
      },
      { validators: [this.differentAirportsValidator]});
      this.loadFlightData();
    }

    this.flightCreateForm.get('departureAirport')!.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((value: string) => {
          return this.airportService.smartSearchAirports(value);
        })
      )
      .subscribe((airports) => {
        this.airports = airports;
      });

    this.flightCreateForm.get('arrivalAirport')!.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((value: string) => {
          return this.airportService.smartSearchAirports(value);
        })
      )
      .subscribe((airports) => {
        this.airports = airports;
      });

    this.flightCreateForm.get('aircraft')!.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((value: string) => {
          return this.aircraftService.smartSearchAircrafts(value);
        })
      )
      .subscribe((aircrafts) => {
        this.aircrafts = aircrafts;
      });
    
    this.calculateDefaultStartDateForDatePicker(); 
  }

  calculateDefaultStartDateForDatePicker(): void {
      const today = new Date();
      const sixMonthsFromNow = new Date();
      sixMonthsFromNow.setMonth(today.getMonth() + 6);
      this.defaultStartDate = sixMonthsFromNow;
  }

  differentAirportsValidator(control: AbstractControl): ValidationErrors | null { 
    const departureAirportControl = control.get('departureAirport');
    const arrivalAirportControl = control.get('arrivalAirport');

    const departureAirport = departureAirportControl?.value;
    const arrivalAirport = arrivalAirportControl?.value
    
    if(departureAirport && arrivalAirport){
      const depCode = departureAirport?.u3digitCode;
      const arrCode = arrivalAirport?.u3digitCode;
      if (depCode && arrCode && depCode === arrCode) {
        arrivalAirportControl.setErrors({ ...arrivalAirport.errors, sameAirport: true });
      } else if (arrivalAirportControl.hasError('sameAirport')) {
          const { sameAirport, ...otherErrors } = arrivalAirportControl.errors || {};
          arrivalAirportControl.setErrors(Object.keys(otherErrors).length ? otherErrors : null);
      }
    }

    return null;
  }

  loadFlightData(): void {
    this.flightCreateForm.patchValue({
      flightNumber: this.flight?.flightNumber,
      departureAirport: this.flight?.departureAirport,
      departureDate: this.flight?.departureTime.split("T")[0],
      departureTime: this.flight?.departureTime.split("T")[1],
      arrivalAirport: this.flight?.arrivalAirport,
      arrivalDate: this.flight?.arrivalTime.split("T")[0],
      arrivalTime: this.flight?.arrivalTime.split("T")[1],
      aircraft: this.flight?.aircraft
    });
  }

  createOrUpdateFlight(): void {
    if(this.flightCreateForm.invalid){
      return;
    }

    const requestBody = (this.isEditMode) ? this.constructPayloadForUpdate() : this.constructPayloadForCreation();

    if(this.isEditMode){
      this.updateFlight(requestBody, this.flight.id);
    } else {
      this.createNewFlight(requestBody);
    }
  }

  disablePastDates = (date: Date | null): boolean => {
    if(!date) return false;
    const today = new Date();
    today.setHours(0,0,0,0);
    const sixMonthsFromNow = new Date();
    sixMonthsFromNow.setMonth(today.getMonth() + 6);
    sixMonthsFromNow.setHours(0, 0, 0, 0);

    return date >= sixMonthsFromNow;
  };

  onDepartingSelected = (e: any): void => {
    const { arrivalDate } = this.flightCreateForm.value;
    const selectedDate= e.value;
    if(arrivalDate!== '' && arrivalDate < selectedDate){
      this.flightCreateForm.patchValue({ arrivalDate: '' });
    }
  }

  validateArrivalDates = (date: Date | null): boolean => {
    if(!date) return false;
    const { departureDate } = this.flightCreateForm.value;
    if(departureDate !== ''){
      return date >= departureDate
    }

    return this.disablePastDates(date);
  }

  constructPayloadForCreation(): any {
    const formData = this.flightCreateForm.value;
    
    return {
      flightNumber: formData.flightNumber,
      departureAirport: formData.departureAirport.u3digitCode,
      departureTime: CommonUtils.formatDateTimeForDateTimePattern(formData.departureDate, formData.departureTime),
      arrivalAirport: formData.arrivalAirport.u3digitCode,
      arrivalTime: CommonUtils.formatDateTimeForDateTimePattern(formData.arrivalDate, formData.arrivalTime),
      aircraftId: formData.aircraft.id
    }
  }

  constructPayloadForUpdate(): any {
    const formData = this.flightCreateForm.value;
    return {
      departureTime: CommonUtils.formatDateTimeForDateTimePattern(formData.departureDate, formData.departureTime),
      arrivalTime: CommonUtils.formatDateTimeForDateTimePattern(formData.arrivalDate, formData.arrivalTime)
    }
  }

  updateFlight(requestBody: any, id: number): void {
    this.flightService.updateFlight(requestBody, id).subscribe({
      next: () => {
        this.snackbar.success('Flight was updated successfully.');
        this.dialogRef.close("success");
      },
      error: (err: any) => {
        this.snackbar.error(`Flight was not updated successfully! ${err?.error?.key}`);
      }
    });
  }

  createNewFlight(requestBody: any): void {
    this.flightService.createFlight(requestBody).subscribe({
      next: () => {
        this.snackbar.success('Flight was created successfully.');
        this.dialogRef.close("success");
      },
      error: (err: any) => {
        this.snackbar.error(`Flight was not created successfully! ${err?.error?.key}`);
      }
    });
  }

  displayAirport(airport: any): string {
    return airport ? `${airport.city} (${airport.u3digitCode})` : '';
  }

  displayAircraft(aircraft: any): string {
    return aircraft ? aircraft.aircraftName : '';
  }

  getLabel(): string {
    return (this.isEditMode) ? "Update Flight" : "Create Flight";
  }

  getButtonLabel(): string {
    return (this.isEditMode) ? "Update" : "Create";
  }

  close(): void {
    this.dialogRef.close();
  }
}
