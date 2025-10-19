import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { UserService } from '../../../services/backend/user.service';
import { UserProfile } from '../../../models/user.models';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CommonUtils} from '../../../utils/common.util';
import { SnackbarService } from '../../../services/frontend/snackbar.service';

@Component({
  selector: 'app-view.profile.modal',
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule
  ],
  templateUrl: './view.profile.modal.component.html',
  styleUrls: ['./view.profile.modal.component.css']
})
export class ViewProfileModalComponent implements OnInit{
  userProfile: UserProfile | null = null;
  profileForm: FormGroup;

  constructor(
    private readonly dialogRef: MatDialogRef<ViewProfileModalComponent>,
    private readonly userService: UserService,
    private readonly formBuilder: FormBuilder,
    private readonly snackbar: SnackbarService
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
    this.loadUserProfile();
  }

  async loadUserProfile(): Promise<void> {
    this.userService.getUserProfile().subscribe({
      next: (profile) => {
        this.userProfile = profile;
        if(this.userProfile !== null) this.patchForm(this.userProfile);
      },
      error: (err) => {
        console.error('Failed to fetch user profile:', err);
      }
    });
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
      this.addresses.push(this.formBuilder.group({
        addressName: [addr.addressName],
        city: [addr.city],
        country: [addr.country]
      }))
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
        this.dialogRef.close();
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

  close(): void {
    this.dialogRef.close();
  }
}
