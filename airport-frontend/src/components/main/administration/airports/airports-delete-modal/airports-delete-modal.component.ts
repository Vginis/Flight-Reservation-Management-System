import { Component, Inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { AirportRepresentation } from '../../../../../models/airport.models';
import { AirportService } from '../../../../../services/backend/airport.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';

@Component({
  selector: 'app-airports-delete-modal',
  imports: [
    MatDialogModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './airports-delete-modal.component.html',
  styleUrl: './airports-delete-modal.component.css'
})
export class AirportsDeleteModalComponent {
  constructor(
      @Inject(MAT_DIALOG_DATA) public readonly airportData: AirportRepresentation,
      private readonly dialogRef: MatDialogRef<AirportsDeleteModalComponent>,
      private readonly airportService: AirportService,
      private readonly snackbar: SnackbarService,
  ) {}

  deleteAirport(): void{
    this.airportService.deleteAirport(this.airportData?.airportId).subscribe({
        next: () => {
          this.snackbar.success("Airport deleted successfully");
          this.dialogRef.close("success");
        },
        error: (err: any) => {
          this.snackbar.error(`Airport was not deleted:${err?.error?.key}`);
        }
      })
  }

  close(): void {
    this.dialogRef.close();
  }
}
