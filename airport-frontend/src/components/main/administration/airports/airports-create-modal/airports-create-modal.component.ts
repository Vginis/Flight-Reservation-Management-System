import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { AirportService } from '../../../../../services/backend/airport.service';

@Component({
  selector: 'app-airports-create-modal',
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
  templateUrl: './airports-create-modal.component.html',
  styleUrl: './airports-create-modal.component.css'
})
export class AirportsCreateModalComponent {
  airportCreateForm: FormGroup;
  
  constructor(
    private readonly dialogRef: MatDialogRef<AirportsCreateModalComponent>,
    private readonly formBuilder: FormBuilder,
    private readonly snackbar: SnackbarService,
    private readonly airportService: AirportService,
  ) {
    this.airportCreateForm = this.formBuilder.group({
      airportName: ['',Validators.required],
      city: ['', Validators.required],
      country: ['', Validators.required],
      u3digitCode: ['', [Validators.required, Validators.pattern("^[A-Z]{3}$")]]
    });
  }

  createNewAirport(): void {
    if(this.airportCreateForm.invalid) return;

    const requestBody = this.constructPayload();
    this.airportService.createAirport(requestBody).subscribe({
      next: () => {
        this.snackbar.success('Airport created successfully.');
        this.dialogRef.close("success");
      },
      error: (err: any) => {
        this.snackbar.error(`Airport was not created successfully! ${err?.error?.key}`);
      }
    }); 
  }

  private constructPayload(): any {
    const formData = this.airportCreateForm.value;
    return {
      airportName: formData.airportName,
      city: formData.city,
      country: formData.country,
      u3digitCode: formData.u3digitCode
    }
  }

  close(): void {
    this.dialogRef.close();
  }
}
