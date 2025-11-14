import { Component, Inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { FlightRepresentation } from '../../../../../models/flight.models';
import { FlightService } from '../../../../../services/backend/flight.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { KeyValuePairs } from '../../../../../models/common.models';

@Component({
  selector: 'app-flight-statusupdate-modal',
  imports: [
      CommonModule,
      MatDialogModule,
      MatButtonModule,
      MatIconModule,
      MatSelectModule
    ],
  templateUrl: './flight-statusupdate-modal.component.html',
  styleUrl: './flight-statusupdate-modal.component.css'
})
export class FlightStatusUpdateModalComponent {
  selectedStatus: string;
  inititalSelectedStatus: string;
  flightStatuses: KeyValuePairs[] = [
    { key: 'DELAYED', value: 'Delayed' }, 
    { key: 'IN_FLIGHT', value: 'In Flight' },
    { key: 'ARRIVED', value: 'Arrived' },
    { key: 'SCHEDULED', value: "Scheduled" }
  ];
  availableStatuses!: KeyValuePairs[];
  allowedTransitions: { [key: string]: string[] } = {
    SCHEDULED: ['DELAYED', 'IN_FLIGHT'],
    DELAYED: ['IN_FLIGHT'],
    IN_FLIGHT: ['ARRIVED'],
    ARRIVED: []
  };

  constructor(
      @Inject(MAT_DIALOG_DATA) public readonly flightData: FlightRepresentation,
      private readonly dialogRef: MatDialogRef<FlightStatusUpdateModalComponent>,
      private readonly flightService: FlightService,
      private readonly snackbar: SnackbarService,
  ) {
    this.selectedStatus = flightData.flightStatus;
    this.inititalSelectedStatus = flightData.flightStatus;
    this.filterAvailableStatuses();
  }

  updateFlightStatus(): void {
    this.flightService.updateFlightStatus(this.flightData?.id, this.selectedStatus).subscribe({
      next: () => {
          this.snackbar.success("Flight status updated successfully");
          this.dialogRef.close("success");
        },
        error: (err: any) => {
          this.snackbar.error(`Flight Status was not updated:${err?.error?.key}`);
        }
    })
  }

  filterAvailableStatuses(): void {
    const allowed = this.allowedTransitions[this.selectedStatus] || [];
    this.availableStatuses = this.flightStatuses.filter(
      s => allowed.includes(s.key) || s.key === this.selectedStatus
    );
  }

  hasSelectedTheSameOption(): boolean {
    return this.selectedStatus === this.inititalSelectedStatus;
  }

  close(): void {
    this.dialogRef.close();
  }
}
