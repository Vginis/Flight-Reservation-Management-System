import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSliderModule } from '@angular/material/slider';
import { AircraftService } from '../../../../../services/backend/aircraft.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { AircraftRepresentation } from '../../../../../models/aircraft.model';

@Component({
  selector: 'app-aircrafts-create-modal',
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatIconModule,
    MatSelectModule,
    MatSliderModule
  ],
  templateUrl: './aircrafts-create-modal.component.html',
  styleUrl: './aircrafts-create-modal.component.css'
})
export class AircraftsCreateModalComponent implements OnInit {
  aircraftCreateForm!: FormGroup;
  seatRows: number[] = [];
  leftSeatColumns: number[] = [];
  rightSeatColumns: number[] = [];
  isEditMode: boolean = false;

  constructor(
    @Inject(MAT_DIALOG_DATA) public readonly aircraft: AircraftRepresentation,
    private readonly formBuilder: FormBuilder,
    private readonly dialogRef: MatDialogRef<AircraftsCreateModalComponent>,
    private readonly aircraftService: AircraftService,
    private readonly snackbar: SnackbarService
  ){
    this.aircraftCreateForm = this.formBuilder.group({
      aircraftName: ['', Validators.required],
      aircraftRows: [1, [Validators.required, Validators.min(1)]],
      aircraftColumns: [6, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    if(this.aircraft){
      this.isEditMode = true;
      this.loadAircraftData();
    }

    this.updateSeatMap();

    this.aircraftCreateForm.valueChanges.subscribe(() => 
      this.updateSeatMap()
    );
  }

  loadAircraftData(): void {
    this.aircraftCreateForm.patchValue({
      aircraftName: this.aircraft?.aircraftName,
      aircraftRows: this.aircraft?.aircraftRows,
      aircraftColumns: this.aircraft?.aircraftColumns
    });
  }

  updateSeatMap(): void {
    const rows = this.aircraftCreateForm.value.aircraftRows;
    const cols = this.aircraftCreateForm.value.aircraftColumns;
    this.seatRows = Array.from({ length: rows});
    const half = Math.floor(cols / 2);
    const left = Math.ceil(cols / 2); 

    this.leftSeatColumns = Array.from({ length: left });
    this.rightSeatColumns = Array.from({ length: half });
  }

  capacity(): number {
    const rows = this.aircraftCreateForm.value.aircraftRows;
    const cols = this.aircraftCreateForm.value.aircraftColumns;
    
    return rows*cols;
  }

  createOrUpdateAircraft(): void {
    if(this.aircraftCreateForm.invalid){
      return;
    }

    const requestBody = { 
      ...this.aircraftCreateForm.value,
      aircraftCapacity: this.capacity()
    }

    if(this.isEditMode){
      this.updateAircraft(requestBody, this.aircraft.id);
    } else {
      this.createNewAircraft(requestBody);
    }
  }

  updateAircraft(requestBody: any, id: number): void {
    this.aircraftService.updateAircraft(requestBody, id).subscribe({
      next: () => {
        this.snackbar.success('Aircraft was updated successfully.');
        this.dialogRef.close("success");
      },
      error: (err: any) => {
        this.snackbar.error(`Aircraft was not updated successfully! ${err?.error?.key}`);
      }
    });
  }

  createNewAircraft(requestBody: any): void {
    this.aircraftService.createAircraft(requestBody).subscribe({
      next: () => {
        this.snackbar.success('Aircraft was created successfully.');
        this.dialogRef.close("success");
      },
      error: (err: any) => {
        this.snackbar.error(`Aircraft was not created successfully! ${err?.error?.key}`);
      }
    });
  }

  getLabel(): string {
    return (this.isEditMode) ? "Update Aircraft" : "Create Aircraft";
  }

  getButtonLabel(): string {
    return (this.isEditMode) ? "Update" : "Create";
  }

  close(): void {
    this.dialogRef.close();
  }
}
