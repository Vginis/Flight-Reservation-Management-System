import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule, MatOptionModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { AirportService } from '../../../../../services/backend/airport.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { debounceTime, distinctUntilChanged, of, switchMap } from 'rxjs';

@Component({
  selector: 'app-flights-filtering',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatOptionModule,
    MatSelectModule,
    MatIconModule,
    FormsModule,
    MatAutocompleteModule,
  ],
  templateUrl: './flights-filtering.component.html',
  styleUrl: './flights-filtering.component.css'
})
export class FlightsFilteringComponent implements OnInit{
  filterOptions = [{key:'flightNumber',label:'Flight Number'}, {key:'flightUUID',label:'Flight Identifier'}];
  flightsFilterForm: FormGroup;
  airports: any = [];

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly airportService: AirportService
  ) {
    this.flightsFilterForm = this.formBuilder.group({
      filterBy: [''],
      filterValue: [''],
      departureAirport: [''],
      departureDate: [''],
      arrivalAirport: [''],
      arrivalDate: ['']
    });
  }

  ngOnInit(): void {
    console.log("HERE");
    this.flightsFilterForm.get('departureAirport')!.valueChanges
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
  }

  disablePastDates = (date: Date | null): boolean => {
    if(!date) return false;
    const today = new Date();
    today.setHours(0,0,0,0);
    return date >= today;
  };

  onDepartingSelected = (e: any): void => {
    const { arrivalDate } = this.flightsFilterForm.value;
    const selectedDate= e.value;
    if(arrivalDate!== '' && arrivalDate < selectedDate){
      this.flightsFilterForm.patchValue({ arrivalDate: '' });
    }
  }

  validateArrivalDates = (date: Date | null): boolean => {
    if(!date) return false;
    const { departureDate } = this.flightsFilterForm.value;
    if(departureDate !== ''){
      return date >= departureDate
    }

    return this.disablePastDates(date);
  }

  displayAirport(airport: any): string {
    return airport ? `${airport.city} (${airport.u3digitCode})` : '';
  }

  onAirportSelected(airport: any): void {
    console.log('Selected airport:', airport);
    // optionally patch or trigger other logic here
  }

  onSubmit(): void {
    console.log('Form submitted', this.flightsFilterForm.value);
  }
}
