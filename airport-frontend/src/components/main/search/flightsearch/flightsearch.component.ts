import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule, provideNativeDateAdapter } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs';
import { AirportService } from '../../../../services/backend/airport.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { FlightService } from '../../../../services/backend/flight.service';
import { FlightSearchHomePageParams } from '../../../../models/common.models';
import { SnackbarService } from '../../../../services/frontend/snackbar.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-flightsearch',
    providers: [provideNativeDateAdapter()],
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatSelectModule,
        MatInputModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatButtonModule,
        MatAutocompleteModule,
        MatIconModule
    ],
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './flightsearch.component.html',
    styleUrls: ['./flightsearch.component.css']
})
export class FlightsearchComponent implements OnInit{
  searchForm: FormGroup;
  airports: any = [];

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly airportService: AirportService,
    private readonly flightService: FlightService,
    private readonly snackBar: SnackbarService,
    private readonly router: Router
  ) {
    this.searchForm = this.formBuilder.group({
      from: [''],
      to: [''],
      departing: [''],
      returning: ['']
    }, 
    { validators: [this.differentAirportsValidator]});
  }

  ngOnInit(): void {
    this.searchForm.get('from')!.valueChanges
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
    this.searchForm.get('to')!.valueChanges
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

  onSubmit() {
    const params: FlightSearchHomePageParams = {
      departureAirport: this.searchForm.value.from,
      arrivalAirport: this.searchForm.value.to,
      departureDate: this.searchForm.value.departing,
      arrivalDate: this.searchForm.value.arriving,
      size: 10,
      index: 0
    }
    this.flightService.searchFlightsHomePage(params).subscribe({
      next: (response: any) => {
        this.router.navigate(['/select-flight'], { state: { flights: response } });
      },
      error: () => {
        this.snackBar.error(`An error has occured. For more information please contact the system administrator.`);
      }
    });
  }

  disablePastDates = (date: Date | null): boolean => {
    if(!date) return false;
    const today = new Date();
    today.setHours(0,0,0,0);
    return date >= today;
  };

  differentAirportsValidator(control: AbstractControl): ValidationErrors | null {
    const departureAirport = control.get('from');
    const arrivalAirport = control.get('to');

    if (departureAirport?.value.u3digitCode!==undefined && arrivalAirport && departureAirport?.value.u3digitCode === arrivalAirport?.value.u3digitCode) {
      arrivalAirport.setErrors({ ...arrivalAirport.errors, sameAirport: true });
    } else if (arrivalAirport?.hasError('sameAirport')) {
      const { sameAirport, ...otherErrors } = arrivalAirport.errors || {};
      arrivalAirport.setErrors(Object.keys(otherErrors).length ? otherErrors : null);
    }
    return null;
  }

  validateArrivalDates = (date: Date | null): boolean => {
    if(!date) return false;
    const { departing } = this.searchForm.value;
    if(departing !== ''){
      return date >= departing
    }

    return this.disablePastDates(date);
  }

  onDepartingSelected = (e: any): void => {
    const { returning } = this.searchForm.value;
    const selectedDate= e.value;
    if(returning!== '' && returning < selectedDate){
      this.searchForm.patchValue({ returning: '' });
    }
  }

  atLeastOneSelected = (): boolean => {
    const { from, to, departing, returning } = this.searchForm.value;
    return !!(from || to || departing || returning);
  }

  displayAirport(airport: any): string {
    return airport ? `${airport.city} (${airport.u3digitCode})` : '';
  }

  formIsNotValid = (): boolean => {
    return !this.atLeastOneSelected() || this.searchForm.invalid;
  }
}
