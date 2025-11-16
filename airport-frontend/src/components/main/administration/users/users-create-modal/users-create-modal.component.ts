import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { UserService } from '../../../../../services/backend/user.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { CommonUtils } from '../../../../../utils/common.util';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { debounceTime, distinctUntilChanged, 
filter, switchMap } from 'rxjs';
import { AirlineService } from '../../../../../services/backend/airline.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { CityCountryService } from '../../../../../services/backend/citycountry.service';

@Component({
  selector: 'app-users-create-modal',
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
  templateUrl: './users-create-modal.component.html',
  styleUrl: './users-create-modal.component.css'
})
export class UsersCreateModalComponent implements OnInit{
  usersCreateForm: FormGroup;
  roles= CommonUtils.RoleObjects;
  selectedFile: File | null = null;
  nonExistingAirline: boolean = true;
  imagePreview: string | null = null;
  logoRequiredError: boolean = true;
  countriesList: string[][] = [];
  citiesList: string[][] = [];

  constructor(
    private readonly dialogRef: MatDialogRef<UsersCreateModalComponent>,
    private readonly userService: UserService,
    private readonly formBuilder: FormBuilder,
    private readonly snackbar: SnackbarService,
    private readonly airlineService: AirlineService,
    private readonly cityCountryService: CityCountryService
  ) {
    this.usersCreateForm = this.formBuilder.group({
      username: ['',Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required,Validators.pattern('\\d{10}')]],
      role: ['', Validators.required],
      addresses: this.formBuilder.array([]),
      passport: [''],
      airlineCode: [''],
      airlineName: [{value: '', disabled: true}]
    })
  }

  ngOnInit(): void {
    this.usersCreateForm.get('role')?.valueChanges.subscribe(role => {
      const { passportControl, airlineCodeControl, airlineNameControl } = this.getDynamicFieldsControls();

      const normalizedRole = role?.toLowerCase();
      if (normalizedRole === 'passenger') {
        this.handleDynamicValidatorsForPassengerForm(passportControl, airlineNameControl, airlineCodeControl);
      } else if (normalizedRole === 'airline_admin') {
        this.handleDynamicValidatorsForAirlineAdminForm(passportControl, airlineNameControl, airlineCodeControl);
      } else {
        this.clearDynamicValidatorForDefaultForm(passportControl, airlineNameControl, airlineCodeControl);
      }

      passportControl?.updateValueAndValidity();
      airlineCodeControl?.updateValueAndValidity();
      airlineNameControl?.updateValueAndValidity();
    });
  }

  private getDynamicFieldsControls(): any{
      const passportControl = this.usersCreateForm.get('passport');
      const airlineCodeControl = this.usersCreateForm.get('airlineCode');
      const airlineNameControl = this.usersCreateForm.get('airlineName');
      return { passportControl, airlineCodeControl, airlineNameControl };
  }

  private handleDynamicValidatorsForPassengerForm(passportControl: AbstractControl, airlineNameControl: AbstractControl, airlineCodeControl: AbstractControl): void {
    passportControl?.setValidators([Validators.required]);

    this.clearValidatorsForFormControl(airlineCodeControl);
    this.clearValidatorsForFormControl(airlineNameControl);
    airlineNameControl?.disable({ emitEvent: false });
  }

  private handleDynamicValidatorsForAirlineAdminForm(passportControl: AbstractControl, airlineNameControl: AbstractControl, airlineCodeControl: AbstractControl): void {
    airlineCodeControl?.setValidators([Validators.required, Validators.pattern('^[A-Z]{2,3}$')]);
    airlineNameControl?.setValidators([Validators.required]);
    airlineNameControl?.disable({ emitEvent: false });

    this.clearValidatorsForFormControl(passportControl);

    this.setupAirlineCodeWatcher(airlineCodeControl, airlineNameControl);
  }

  private clearDynamicValidatorForDefaultForm(passportControl: AbstractControl, airlineNameControl: AbstractControl, airlineCodeControl: AbstractControl){
    this.clearValidatorsForFormControl(passportControl);
    this.clearValidatorsForFormControl(airlineCodeControl);
    this.clearValidatorsForFormControl(airlineNameControl);
  }

  private setupAirlineCodeWatcher(airlineCodeControl: AbstractControl, airlineNameControl: AbstractControl): void {
    airlineCodeControl.valueChanges.pipe(
      debounceTime(400),distinctUntilChanged(),
      filter(() => airlineCodeControl.valid),
      switchMap((code: string) => 
        this.airlineService.searchAirlines({
          searchField: 'u2digitCode',
          searchValue: code,
          index: 0,
          size: 1,
          sortDirection: 'asc',
          sortBy: 'airlineName'
        })
      )
    ).subscribe((airlineData: any) => {
      if(airlineData && airlineData.total > 0) {
        airlineNameControl.setValue(airlineData?.results[0]?.airlineName);
        airlineNameControl.disable({ emitEvent: false });
        this.nonExistingAirline = false;
        this.mapFileRepresentationToObject(airlineData?.results[0]?.fileRepresentation);

        const reader = new FileReader();
        reader.onload = () => {
            this.imagePreview = reader.result as string;
        };
        if(this.selectedFile!==null){
          reader.readAsDataURL(this.selectedFile);
        }
        
      } else if(airlineData?.total===0){
        airlineNameControl.reset();
        airlineNameControl.enable({ emitEvent: false });
        this.nonExistingAirline = true;
        this.selectedFile = null;
        this.imagePreview = '';
      }
    })
  }

  private clearValidatorsForFormControl(formFieldControl: AbstractControl): void {
    formFieldControl?.clearValidators();
    formFieldControl?.setValue('');
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

  createNewUser(): void {
    if(this.usersCreateForm.invalid) return;
    
    const { requestBody, role } = this.constructPayload();

    if(role === CommonUtils.AIRLINE_ADMIN){
      this.logoRequiredError = this.selectedFile === null;
      if(this.logoRequiredError) {
        return;
      }

      const formData = new FormData();
      formData.append(
        'airlineAdministratorCreateRepresentation',
        new Blob([JSON.stringify(requestBody)], { type: 'application/json' })
      );

      if (this.selectedFile) {
        formData.append('airlineLogo', this.selectedFile);
      }

      this.userService.createAirlineAdministrator(formData).subscribe({
        next: () => {
          this.snackbar.success('User created successfully and will be informed by email.');
          this.dialogRef.close("success");
        },
        error: (err: any) => {
          this.snackbar.error(`User was not created successfully! ${err?.error?.key}`);
        }
      });
    } else {
      this.userService.createUser(requestBody, role).subscribe({
        next: () => {
          this.snackbar.success('User created successfully and will be informed by email.');
          this.dialogRef.close("success");
        },
        error: (err: any) => {
          this.snackbar.error(`User was not created successfully! ${err?.error?.key}`);
        }
      }); 
    }
  }

  private constructPayload(): any{
    const formData = this.usersCreateForm.value;
    let requestBody: any = {
      username: formData.username,
      firstName: formData.firstName,
      lastName: formData.lastName,
      email: formData.email,
      phoneNumber: formData.phoneNumber,
      addresses: formData.addresses
    }

    const role = formData.role;
    if(role === CommonUtils.PASSENGER){
      requestBody.passport = formData.passport 
    }

    if(role === CommonUtils.AIRLINE_ADMIN){
      const airlineName = formData.airlineName;
      const u2digitCode = formData.airlineCode;
      const airlineRepresentation = {
        airlineName: airlineName,
        u2digitCode: u2digitCode
      }
      requestBody.airlineCreateRepresentation = airlineRepresentation;
    }
        
    return { requestBody, role };
  }

  isPassenger(): boolean {
    const role = this.usersCreateForm.get("role")?.value as string;
    return role === CommonUtils.PASSENGER;
  }

  isAirlineAdmin(): boolean {
    const role = this.usersCreateForm.get("role")?.value as string;
    return role === CommonUtils.AIRLINE_ADMIN;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result as string;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  mapFileRepresentationToObject(fileData: any): void {
    const byteCharacters = atob(fileData.content);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.codePointAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: fileData.filetype });
    this.selectedFile = new File([blob], fileData.filename, { type: fileData.filetype });
  }

  get addresses(): FormArray {
    return this.usersCreateForm.get('addresses') as FormArray;
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
