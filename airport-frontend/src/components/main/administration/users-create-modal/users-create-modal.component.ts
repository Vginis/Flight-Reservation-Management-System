import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { UserService } from '../../../../services/backend/user.service';
import { SnackbarService } from '../../../../services/frontend/snackbar.service';
import { CommonUtils } from '../../../../utils/common.util';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { catchError, debounceTime, distinctUntilChanged, EMPTY, filter, switchMap } from 'rxjs';
import { AirlineService } from '../../../../services/backend/airline.service';
import { SearchParams } from '../../../../models/common.models';

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
    MatSelectModule
  ],
  templateUrl: './users-create-modal.component.html',
  styleUrl: './users-create-modal.component.css'
})
export class UsersCreateModalComponent implements OnInit{
  usersCreateForm: FormGroup;
  roles= CommonUtils.RoleObjects;

  constructor(
    private readonly dialogRef: MatDialogRef<UsersCreateModalComponent>,
    private readonly userService: UserService,
    private readonly formBuilder: FormBuilder,
    private readonly snackbar: SnackbarService,
    private readonly airlineService: AirlineService
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
        this.handleDynamicValidatorsForPassengerForm(passportControl!, airlineNameControl!, airlineCodeControl!);
      } else if (normalizedRole === 'airline_admin') {
        this.handleDynamicValidatorsForAirlineAdminForm(passportControl!, airlineNameControl!, airlineCodeControl!);
      } else {
        this.clearDynamicValidatorForDefaultForm(passportControl!, airlineNameControl!, airlineCodeControl!);
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
      //   .pipe(
      //   catchError((err:any) => {
      //     if (err.status === 404) {
      //       airlineNameControl.reset();
      //       airlineNameControl.enable({ emitEvent: false });
      //     }
      //     return EMPTY;
      //   })
      // )
    )
    ).subscribe((airlineData: any) => {
      if(airlineData && airlineData.total > 0) {
        airlineNameControl.setValue(airlineData?.results[0]?.airlineName);
        airlineNameControl.disable({ emitEvent: false });
      } else if(airlineData && airlineData.total===0){
        airlineNameControl.reset();
        airlineNameControl.enable({ emitEvent: false });
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
      city: ['',Validators.required],
      country: ['',Validators.required]
    });
    this.addresses.push(addressGroup);
  }

  removeAddress(index: number): void {
    this.addresses.removeAt(index);  
  }

  createNewUser(): void {
    if(this.usersCreateForm.invalid) return;
    
    const { requestBody, role } = this.constructPayload();
    this.userService.createUser(requestBody, role).subscribe({
      next: () => {
        this.snackbar.success('User created successfully and will be informed by email.');
        this.dialogRef.close();
      },
      error: (err: any) => {
        console.error(err);
        this.snackbar.error('User was not created successfully!');
      }
    });  
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

  get addresses(): FormArray {
    return this.usersCreateForm.get('addresses') as FormArray;
  }

  close(): void {
    this.dialogRef.close();
  }
}
