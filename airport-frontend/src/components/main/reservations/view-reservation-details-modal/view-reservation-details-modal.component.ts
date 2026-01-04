import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-view-reservation-details-modal',
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatDividerModule,
    CommonModule,
    MatIconModule,
    MatCardModule
  ],
  templateUrl: './view-reservation-details-modal.component.html',
  styleUrl: './view-reservation-details-modal.component.css'
})
export class ViewReservationDetailsModalComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) public readonly reservation: any,
    private readonly dialogRef: MatDialogRef<ViewReservationDetailsModalComponent>,
    ){
  }

  formatDateTime(value: string): string {
    const date = new Date(value);
    return date.toLocaleDateString(undefined, {
      day: '2-digit',
      month: 'short',
      year: 'numeric'
    }) + ' • ' + date.toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  close(): void {
    this.dialogRef.close();
  }
}
