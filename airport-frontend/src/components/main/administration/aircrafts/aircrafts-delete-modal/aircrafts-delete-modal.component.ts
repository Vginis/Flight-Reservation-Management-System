import { Component, Inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { AircraftRepresentation } from '../../../../../models/aircraft.model';
import { AircraftService } from '../../../../../services/backend/aircraft.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';

@Component({
  selector: 'app-aircrafts-delete-modal',
  imports: [
    MatDialogModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './aircrafts-delete-modal.component.html',
  styleUrl: './aircrafts-delete-modal.component.css'
})
export class AircraftsDeleteModalComponent {
  constructor(
      @Inject(MAT_DIALOG_DATA) public readonly aircraft: AircraftRepresentation,
      private readonly dialogRef: MatDialogRef<AircraftsDeleteModalComponent>,
      private readonly aircraftService: AircraftService,
      private readonly snackbar: SnackbarService,
  ) {}

  deleteAircraft(): void{
    this.aircraftService.deleteAircraft(this.aircraft?.id).subscribe({
        next: () => {
          this.snackbar.success("Aircraft deleted successfully");
          this.dialogRef.close("success");
        },
        error: (err: any) => {
          this.snackbar.error(`Aircraft was not deleted:${err?.error?.key}`);
        }
      })
  }

  close(): void {
    this.dialogRef.close();
  }
}
