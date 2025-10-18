import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { UserService } from '../../../../services/backend/user.service';
import { SnackbarService } from '../../../../services/frontend/snackbar.service';
import { CommonUtils } from '../../../../utils/common.util';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';

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
    private readonly snackbar: SnackbarService
  ) {
    this.usersCreateForm = this.formBuilder.group({
      username: ['',Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required,Validators.pattern('\\d{10}')]],
      role: ['', Validators.required],
      addresses: this.formBuilder.array([]),
      passport: ['']
    })
  }

  ngOnInit(): void {
    this.usersCreateForm.get('role')?.valueChanges.subscribe(role => {
      const passportControl = this.usersCreateForm.get('passport');

      if (role?.toLowerCase() === 'passenger') {
        passportControl?.setValidators([Validators.required]);
      } else {
        passportControl?.clearValidators();
        passportControl?.setValue('');
      }

      passportControl?.updateValueAndValidity();
    });
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
      return;
    }
        
    return { requestBody, role };
  }

  isPassenger(): boolean {
    const role = this.usersCreateForm.get("role")?.value as string;
    return role === CommonUtils.PASSENGER;
  }

  get addresses(): FormArray {
    return this.usersCreateForm.get('addresses') as FormArray;
  }

  close(): void {
    this.dialogRef.close();
  }
}
