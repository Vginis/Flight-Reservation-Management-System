import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { FlightRepresentation } from '../../../../../models/flight.models';
import { FlightService } from '../../../../../services/backend/flight.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-flights-cancel-modal',
  imports: [
    MatDialogModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './flights-cancel-modal.component.html',
  styleUrl: './flights-cancel-modal.component.css'
})
export class FlightsCancelModalComponent {
  constructor(
      @Inject(MAT_DIALOG_DATA) public readonly flightData: FlightRepresentation,
      private readonly dialogRef: MatDialogRef<FlightsCancelModalComponent>,
      private readonly flightService: FlightService,
      private readonly snackbar: SnackbarService,
  ) {}

  cancelFlight(): void{
    this.flightService.updateFlightStatus(this.flightData?.id, "CANCELLED").subscribe({
        next: () => {
          this.snackbar.success("Flight cancelled successfully");
          this.dialogRef.close("success");
        },
        error: (err: any) => {
          this.snackbar.error(`Flight was not cancelled:${err?.error?.key}`);
        }
      })
  }

  close(): void {
    this.dialogRef.close();
  }
}
