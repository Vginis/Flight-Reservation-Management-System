import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { UserService } from '../../../services/backend/user.service';

@Component({
  selector: 'app-view.profile.modal',
  imports: [CommonModule, MatDialogModule, MatButtonModule, MatIconModule],
  templateUrl: './view.profile.modal.component.html',
  styleUrls: ['./view.profile.modal.component.css']
})
export class ViewProfileModalComponent implements OnInit{
  userProfile: any = null;//todo replace any with model
  loading = true;

  constructor(
    private readonly dialogRef: MatDialogRef<ViewProfileModalComponent>,
    private readonly userService: UserService
  ) {
  }
  
  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    this.userService.getUserProfile().subscribe({
      next: (profile) => {
        console.log(profile);
        this.userProfile = profile;
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to fetch user profile:', err);
        this.loading = false;
      }
    });
  }

  close(): void {
    this.dialogRef.close();
  }
}
