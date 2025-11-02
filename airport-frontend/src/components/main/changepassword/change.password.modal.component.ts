import { Component } from '@angular/core';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { UserService } from '../../../services/backend/user.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SnackbarService } from '../../../services/frontend/snackbar.service';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-change.password.modal',
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule
  ],
  templateUrl: './change.password.modal.component.html',
  styleUrl: './change.password.modal.component.css'
})
export class ChangePasswordModalComponent {
  resetPasswordForm: FormGroup;

  constructor(
    private readonly dialogRef: MatDialogRef<ChangePasswordModalComponent>,
    private readonly userService: UserService,
    private readonly formBuilder: FormBuilder,
    private readonly snackbar: SnackbarService
  ) {
    this.resetPasswordForm = this.formBuilder.group({
      newPassword: ['', [
        Validators.required, 
        Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$'),
      ]],
      newPasswordConfirmation: ['', Validators.required]
    },{
      validators: [this.passwordMatchValidator]
    })
  }

  resetUserPassword(): void {
    if(this.resetPasswordForm.invalid) {
      this.resetPasswordForm.markAllAsTouched();
      return;}

    const formData = this.resetPasswordForm.value;
    const requestBody = {
      newPassword: formData.newPassword 
    };
    
    this.userService.resetPassword(requestBody).subscribe({
      next: () => {
        this.snackbar.success('Passworded reseted successfully!');
        this.dialogRef.close();
      },
      error: (err: any) => {
        console.error(err);
        this.snackbar.error(`Password was not updated successfully! ${err?.error?.key}`);
      }
    });  
  }

  passwordMatchValidator(formGroup: FormGroup){
    const password = formGroup.get('newPassword')?.value;
    const confirm = formGroup.get('newPasswordConfirmation')?.value;

    return password === confirm ? null : { passwordsMismatch: true };
  }

  close(): void {
    this.dialogRef.close();
  }
}
