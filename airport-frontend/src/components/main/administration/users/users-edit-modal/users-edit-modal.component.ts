import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { UserService } from '../../../../../services/backend/user.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { CommonUtils } from '../../../../../utils/common.util';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { UserProfile } from '../../../../../models/user.models';

@Component({
  selector: 'app-users-edit-modal',
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
  templateUrl: './users-edit-modal.component.html',
  styleUrl: './users-edit-modal.component.css'
})
export class UsersEditModalComponent implements OnInit{
  userUpdateForm: FormGroup;
  constructor(
    @Inject(MAT_DIALOG_DATA) public readonly userProfileData: UserProfile,
    private readonly dialogRef: MatDialogRef<UsersEditModalComponent>,
    private readonly userService: UserService,
    private readonly formBuilder: FormBuilder,
    private readonly snackbar: SnackbarService,
  ){
    this.userUpdateForm = this.formBuilder.group({
      username: [{value: '', disabled: true}, Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required,Validators.pattern('\\d{10}')]],
      addresses: this.formBuilder.array([]),
      passport: [''],
    })
  }

  ngOnInit(): void {
    if(this.isPassenger()){
      const passportControl = this.userUpdateForm.get('passport');
      passportControl?.setValidators([Validators.required]);
      passportControl?.updateValueAndValidity();
    }

    this.patchForm(this.userProfileData)
  }

  updateUser(): void {
    if(this.userUpdateForm.invalid){
      return;
    }

    const requestBody = this.constructPayload();
    
    this.userService.updateUserDetails(requestBody, this.userProfileData?.username, this.userProfileData.role).subscribe({
      next: () => {
        this.snackbar.success('User Details updated successfully!');
        this.dialogRef.close("success");
      },
      error: (err: any) => {
        this.snackbar.error(`User Details were not updated successfully! ${err?.error?.key}`);
      }
    });  
  }

  private constructPayload(){
    const formData = this.userUpdateForm.value;
    let payload:any = {
      firstName: formData.firstName,
      lastName: formData.lastName,
      email: formData.email,
      phoneNumber: formData.phoneNumber,
      addresses: formData.addresses
    }

    if(this.isPassenger()){
      payload.passport = formData.passport;
    }
    return payload;
  }

  private patchForm(userProfile: UserProfile | null){
    if(!userProfile) return;

    this.userUpdateForm.patchValue({
      username: userProfile?.username,
      firstName: userProfile?.firstName,
      lastName: userProfile?.lastName,
      email: userProfile?.email,
      phoneNumber: userProfile?.phoneNumber,
    });

    this.addresses.clear();
    userProfile?.addresses?.forEach((addr: any) => {
      this.addresses.push(this.formBuilder.group({
        addressName: [addr.addressName],
        city: [addr.city],
        country: [addr.country]
      }))
    });

    if(this.isPassenger()){
      this.userService.getPassengerPassport(userProfile?.username).subscribe({
        next: (response: any) => {
          const passport = response?.passport ?? '';
          this.userUpdateForm.patchValue({ passport })
        },
        error: (err: any) => {
          this.snackbar.error(`Passenger passport was not retrieved:${err?.error?.key}`);
        }
      })
    }
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

  isPassenger(): boolean {
    return this.userProfileData.role === CommonUtils.PASSENGER;
  }

  get addresses(): FormArray {
    return this.userUpdateForm.get('addresses') as FormArray;
  }

  close(): void {
    this.dialogRef.close();
  }
}
