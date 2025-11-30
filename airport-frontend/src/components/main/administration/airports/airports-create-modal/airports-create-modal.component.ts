import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { AirportService } from '../../../../../services/backend/airport.service';
import { CityCountryService } from '../../../../../services/backend/citycountry.service';
import { debounceTime, distinctUntilChanged, filter, switchMap } from 'rxjs';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

@Component({
  selector: 'app-airports-create-modal',
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatIconModule,
    MatSelectModule,
    MatAutocompleteModule
  ],
  templateUrl: './airports-create-modal.component.html',
  styleUrl: './airports-create-modal.component.css'
})
export class AirportsCreateModalComponent implements OnInit {
  airportCreateForm: FormGroup;
  countriesList: string[] = [];
  citiesList: string[] = [];

  constructor(
    private readonly dialogRef: MatDialogRef<AirportsCreateModalComponent>,
    private readonly formBuilder: FormBuilder,
    private readonly snackbar: SnackbarService,
    private readonly airportService: AirportService,
    private readonly cityCountryService: CityCountryService
  ) {
    this.airportCreateForm = this.formBuilder.group({
      airportName: ['',Validators.required],
      city: ['', Validators.required],
      country: ['', Validators.required],
      u3digitCode: ['', [Validators.required, Validators.pattern("^[A-Z]{3}$")]]
    });
  }

  ngOnInit(): void {
    this.airportCreateForm.get('country')?.valueChanges
      .pipe(
        filter((value): value is string => value !== null),
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((value: string) => {
          return this.cityCountryService.smartSearchCountries(value);
        })
      )
      .subscribe((countries) => {
        this.countriesList = countries;
        this.airportCreateForm.get('country')!.setValidators([
          Validators.required,
          this.validateAutocompleteCityCountryOption(countries, 'Country')
        ]);
        this.airportCreateForm.get('country')!.updateValueAndValidity({ emitEvent: false });
      });

    this.airportCreateForm.get('city')?.valueChanges
      .pipe(
        filter((value): value is string => value !== null),
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((value: string) => {
          return this.cityCountryService.smartSearchCities(value, null);
        })
      )
      .subscribe((cities) => {
        this.citiesList = cities;
        this.airportCreateForm.get('city')!.setValidators([
          Validators.required,
          this.validateAutocompleteCityCountryOption(cities, 'City')
        ]);
        this.airportCreateForm.get('city')!.updateValueAndValidity({ emitEvent: false });
      });
  }
  
  createNewAirport(): void {
    if(this.airportCreateForm.invalid) return;

    const requestBody = this.constructPayload();
    this.airportService.createAirport(requestBody).subscribe({
      next: () => {
        this.snackbar.success('Airport created successfully.');
        this.dialogRef.close("success");
      },
      error: (err: any) => {
        this.snackbar.error(`Airport was not created successfully! ${err?.error?.key}`);
      }
    }); 
  }

  private constructPayload(): any {
    const formData = this.airportCreateForm.value;
    return {
      airportName: formData.airportName,
      city: formData.city,
      country: formData.country,
      u3digitCode: formData.u3digitCode
    }
  }

  validateAutocompleteCityCountryOption(options: string[], formType: string) {
    return (control: AbstractControl) => {
      const value = control.value;
      if(!value) return null;

      if(formType === 'Country'){
        return options.includes(value) ? null : { invalidCountryAutoComplete: true }
      } else {
        return options.includes(value) ? null : { invalidCityAutoComplete: true }
      }
    }
  }

  displayOption(option: string) { 
    return option;
  }

  close(): void {
    this.dialogRef.close();
  }
}
