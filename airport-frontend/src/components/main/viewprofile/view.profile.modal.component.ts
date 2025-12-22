import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { UserService } from '../../../services/backend/user.service';
import { UserProfile } from '../../../models/user.models';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CommonUtils} from '../../../utils/common.util';
import { SnackbarService } from '../../../services/frontend/snackbar.service';
import { debounceTime, distinctUntilChanged, filter, switchMap } from 'rxjs';
import { CityCountryService } from '../../../services/backend/citycountry.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

@Component({
  selector: 'app-view.profile.modal',
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatAutocompleteModule
  ],
  templateUrl: './view.profile.modal.component.html',
  styleUrls: ['./view.profile.modal.component.css']
})
export class ViewProfileModalComponent implements OnInit{
  profileForm: FormGroup;
  countriesList: string[][] = [];
  citiesList: string[][] = [];

  constructor(
    @Inject(MAT_DIALOG_DATA) public readonly userProfile: UserProfile,
    private readonly dialogRef: MatDialogRef<ViewProfileModalComponent>,
    private readonly userService: UserService,
    private readonly formBuilder: FormBuilder,
    private readonly snackbar: SnackbarService,
    private readonly cityCountryService: CityCountryService
  ) {
    this.profileForm = this.formBuilder.group({
      username: [{value: '', disabled: true},Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required,Validators.pattern('\\d{10}')]],
      role: [{value: '', disabled: true}, Validators.required],
      addresses: this.formBuilder.array([])
    })
  }
  
  ngOnInit(): void {
    if(this.userProfile) {
      this.patchForm(this.userProfile);
    }
  }

  private patchForm(userProfile: UserProfile | null){
    this.profileForm.patchValue({
      username: userProfile?.username,
      firstName: userProfile?.firstName,
      lastName: userProfile?.lastName,
      email: userProfile?.email,
      phoneNumber: userProfile?.phoneNumber,
      role: CommonUtils.mapRoleToLabel(userProfile?.role ?? '')
    });

    this.addresses.clear();
    userProfile?.addresses?.forEach((addr: any) => {
      this.addresses.push(this.createAddressGroup(addr));
    })
  }

  updateUserProfile(): void{
    if(this.profileForm.invalid){
      return;
    }

    const requestBody = this.constructPayload();
    this.userService.updateUserProfile(requestBody).subscribe({
      next: () => {
        this.snackbar.success('Profile updated successfully!');
        this.dialogRef.close("success");
      },
      error: (err: any) => {
        console.error(err);
        this.snackbar.error(`Profile was not updated successfully! ${err?.error?.key}`);
      }
    });  
  }

  private constructPayload(){
    const formData = this.profileForm.value;
    return {
      firstName: formData.firstName,
      lastName: formData.lastName,
      email: formData.email,
      phoneNumber: formData.phoneNumber,
      addresses: formData.addresses
    }
  }

  get addresses(): FormArray {
    return this.profileForm.get('addresses') as FormArray;
  }

  private createAddressGroup(addr?: any): FormGroup {
    const group = this.formBuilder.group({
      addressName: [addr?.addressName || '', Validators.required],
      city: [{ value: addr?.city || '', disabled: true }, Validators.required],
      country: [addr?.country || '', Validators.required]
    });

    const index = this.addresses.length;

    this.countriesList[index] = [];
    this.citiesList[index] = [];

    this.enableCityBasedOnCountryListener(group, index);

    group.get('country')!.valueChanges
      .pipe(
        filter((value): value is string => value !== null),
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(value =>
          this.cityCountryService.smartSearchCountries(value)
        )
      )
      .subscribe(countries => {
        this.countriesList[index] = countries;
        group.get('country')!.setValidators([
          Validators.required,
          this.validateAutocompleteCountryOption(countries, 'Country')
        ]);
        group.get('country')!.updateValueAndValidity({ emitEvent: false });
      });

    group.get('city')!.valueChanges
      .pipe(
        filter((value): value is string => value !== null),
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(value => {
          const countryValue = group.get('country')!.value;
          return this.cityCountryService.smartSearchCities(value, countryValue);
        })
      )
      .subscribe(cities => {
        this.citiesList[index] = cities;
        group.get('city')!.setValidators([
          Validators.required,
          this.validateAutocompleteCountryOption(cities, 'City')
        ]);
        group.get('city')!.updateValueAndValidity({ emitEvent: false });
      });

    if (addr?.country) {
      group.get('city')!.enable();
    }

    return group;
    }


  addAddress(): void {
    if(this.addresses.length==2){
      return;
    }
    
    this.addresses.push(this.createAddressGroup());
  }
  
  enableCityBasedOnCountryListener(group: FormGroup, index: number) {
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

  close(): void {
    this.dialogRef.close();
  }
}
