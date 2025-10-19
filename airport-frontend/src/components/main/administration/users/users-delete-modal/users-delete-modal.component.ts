import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { UserProfile } from '../../../../../models/user.models';
import { UsersEditModalComponent } from '../users-edit-modal/users-edit-modal.component';
import { UserService } from '../../../../../services/backend/user.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-users-delete-modal',
  imports: [
    MatDialogModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './users-delete-modal.component.html',
  styleUrl: './users-delete-modal.component.css'
})
export class UsersDeleteModalComponent {
  constructor(
      @Inject(MAT_DIALOG_DATA) public readonly userProfileData: UserProfile,
      private readonly dialogRef: MatDialogRef<UsersEditModalComponent>,
      private readonly userService: UserService,
      private readonly snackbar: SnackbarService,
  ) {}

  deleteUser(): void{
    this.userService.deleteUser(this.userProfileData?.id).subscribe({
        next: (response: any) => {
          this.snackbar.success("User deleted successfully");
          this.dialogRef.close("success");
        },
        error: (err: any) => {
          this.snackbar.error(`User was not deleted:${err?.error?.key}`);
        }
      })
  }

  close(): void {
    this.dialogRef.close();
  }
}
