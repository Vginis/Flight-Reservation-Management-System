import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../services/backend/user.service';
import { IdentityService } from '../../../services/keycloak/identity.service';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { debounceTime, distinctUntilChanged, filter, switchMap } from 'rxjs';
import { CityCountryService } from '../../../services/backend/citycountry.service';
import { SnackbarService } from '../../../services/frontend/snackbar.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-complete-registration',
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatAutocompleteModule
  ],
  templateUrl: './complete-registration.component.html',
  styleUrl: './complete-registration.component.css'
})
export class CompleteRegistrationComponent implements OnInit {
  completeRegistrationForm: FormGroup;
  countriesList: string[][] = [];
  citiesList: string[][] = [];

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly userService: UserService,
    private readonly identityService: IdentityService,
    private readonly cityCountryService: CityCountryService,
    private readonly snackbar: SnackbarService,
    private readonly router: Router,
  ) {
    this.completeRegistrationForm = this.formBuilder.group({
      username: [{value: '', disabled: true},Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required,Validators.pattern('\\d{10}')]],
      passport: ['', Validators.required],
      addresses: this.formBuilder.array([])
    });
  }

  ngOnInit(): void {
    const keycloakProfile = this.identityService.getKeycloakProfileIdentity();
    this.completeRegistrationForm.patchValue({
      username: keycloakProfile?.username,
      firstName: keycloakProfile?.firstName,
      lastName: keycloakProfile?.lastName,
      email: keycloakProfile?.email,
    });
  }

  completeRegistration(): void {
    if(this.completeRegistrationForm.invalid) return ;

    const requestBody = this.constructPayload();

    this.userService.completePassengerRegistration(requestBody).subscribe({
      next: () => {
        this.navigateToHomePage();
      },
      error: (err: any) => {
        this.snackbar.error(`Your account is not successfully registered! ${err?.error?.key}`);
      }
    });
  }

  private constructPayload(): any{
    const formData = this.completeRegistrationForm.value;
    return {
      username: "temporary",
      firstName: formData.firstName,
      lastName: formData.lastName,
      email: formData.email,
      phoneNumber: formData.phoneNumber,
      passport: formData.passport,
      addresses: formData.addresses
    }
  }

  get addresses(): FormArray {
    return this.completeRegistrationForm.get('addresses') as FormArray;
  }

  addAddress(): void {
      if(this.addresses.length==2){
        return;
      }
      const addressGroup = this.formBuilder.group({
        addressName: ['',Validators.required],
        city: [{ value: '', disabled: true },[
          Validators.required,
          this.validateAutocompleteCountryOption(this.citiesList[this.addresses.length] || [],
            'City'
          )
        ]],
        country: ['',[
          Validators.required,
          this.validateAutocompleteCountryOption(this.countriesList[this.addresses.length] || [],
            'Country'
          )
        ]]
      });
      this.addresses.push(addressGroup);
  
      const index = this.addresses.length - 1;
  
      this.enableCityBasedOnCountryListener(index);
      this.countriesList[index] = [];
      this.citiesList[index] = [];
      addressGroup.get('country')!.valueChanges
        .pipe(
          filter((value): value is string => value !== null),
          debounceTime(300),
          distinctUntilChanged(),
          switchMap((value: string) => {
            return this.cityCountryService.smartSearchCountries(value);
          })
        )
        .subscribe((countries) => {
          this.countriesList[index] = countries;
          addressGroup.get('country')!.setValidators([
            Validators.required,
            this.validateAutocompleteCountryOption(countries, 'Country')
          ]);
          addressGroup.get('country')!.updateValueAndValidity({ emitEvent: false });
        });
      
      addressGroup.get('city')!.valueChanges
        .pipe(
          filter((value): value is string => value !== null),
          debounceTime(300),
          distinctUntilChanged(),
          switchMap((value: string) => {
            const countryValue = addressGroup.get('country')?.value;
            return this.cityCountryService.smartSearchCities(value, countryValue);
          })
        )
        .subscribe((cities) => {
          this.citiesList[index] = cities;
          addressGroup.get('city')!.setValidators([
            Validators.required,
            this.validateAutocompleteCountryOption(cities, 'City')
          ]);
          addressGroup.get('city')!.updateValueAndValidity({ emitEvent: false });
        });
    }
  
  enableCityBasedOnCountryListener(index: number) {
    const group = this.addresses.at(index);

    group.get('country')!.valueChanges.subscribe(value => {
      const validCountries = this.countriesList[index] ?? [];

      const isValid = validCountries.includes(value);

      if (isValid) {
        group.get('city')!.enable();
      } else {
        group.get('city')!.reset();
        group.get('city')!.disable();
      }
    });
  }
  
  removeAddress(index: number): void {
    this.addresses.removeAt(index);  
  }

  displayCityCountry(cityCountry: any): string {
    return cityCountry;
  }

  validateAutocompleteCountryOption(options: string[], formType: string) {
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

  navigateToHomePage() {
    this.snackbar.success('Your account is successfully registered!');
    setTimeout(() => {
      window.location.href = '/';
    }, 3000);
  }
}
