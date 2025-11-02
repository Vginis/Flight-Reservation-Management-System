import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { AirportService } from '../../../../../services/backend/airport.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { AirportRepresentation } from '../../../../../models/airport.models';

@Component({
  selector: 'app-airports-edit-modal',
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatIconModule,
    MatSelectModule
  ],
  templateUrl: './airports-edit-modal.component.html',
  styleUrl: './airports-edit-modal.component.css'
})
export class AirportsEditModalComponent implements OnInit{
  airportUpdateForm: FormGroup;
  constructor(
    @Inject(MAT_DIALOG_DATA) public readonly airportData: AirportRepresentation,
    private readonly dialogRef: MatDialogRef<AirportsEditModalComponent>,
    private readonly airportService: AirportService,
    private readonly formBuilder: FormBuilder,
    private readonly snackbar: SnackbarService,
  ){
    this.airportUpdateForm = this.formBuilder.group({
      airportName: ['', Validators.required],
      city: ['', Validators.required],
      country: ['', Validators.required],
      u3digitCode: [{value: '', disabled: true}, [Validators.required, Validators.pattern('^[A-Z]{3}$')]],
    });
  }

  ngOnInit(): void {
    this.patchForm(this.airportData);
  }

  updateAirport(): void {
    if(this.airportUpdateForm.invalid) return;

    const requestBody = this.constructPayload();
    
    this.airportService.updateAirport(requestBody).subscribe({
      next: () => {
        this.snackbar.success('Airport Details updated successfully!');
        this.dialogRef.close("success");
      },
      error: (err: any) => {
        this.snackbar.error(`Airport Details were not updated successfully! ${err?.error?.key}`);
      }
    });  
  }

  private patchForm(airportRepresentation: AirportRepresentation | null): void {
    if(!this.airportUpdateForm.invalid) return;

    this.airportUpdateForm.patchValue({
      airportName: airportRepresentation?.airportName,
      city: airportRepresentation?.city,
      country: airportRepresentation?.country,
      u3digitCode: airportRepresentation?.u3digitCode
    });
  }

  private constructPayload(): any{
    const formData = this.airportUpdateForm.value;
    return {
      id: this.airportData.airportId,
      airportName: formData.airportName,
      city: formData.city,
      country: formData.country,
      u3digitCode: this.airportData.u3digitCode,
    }
  }

  close(): void {
    this.dialogRef.close();
  }
}
