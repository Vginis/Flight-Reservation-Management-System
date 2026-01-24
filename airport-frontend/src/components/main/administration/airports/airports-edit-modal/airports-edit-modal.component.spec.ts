import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AirportsEditModalComponent } from './airports-edit-modal.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AirportService } from '../../../../../services/backend/airport.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { AirportRepresentation } from '../../../../../models/airport.models';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('AirportsEditModalComponent', () => {
  let component: AirportsEditModalComponent;
  let fixture: ComponentFixture<AirportsEditModalComponent>;

  let airportServiceSpy: jasmine.SpyObj<AirportService>;
  let snackbarSpy: jasmine.SpyObj<SnackbarService>;
  let dialogRefSpy: jasmine.SpyObj<MatDialogRef<AirportsEditModalComponent>>;

  const mockAirport: AirportRepresentation = {
    airportId: 1,
    airportName: 'JFK',
    city: 'New York',
    country: 'USA',
    u3digitCode: 'JFK'
  } as AirportRepresentation;

  beforeEach(async () => {
    airportServiceSpy = jasmine.createSpyObj('AirportService', ['updateAirport']);
    snackbarSpy = jasmine.createSpyObj('SnackbarService', ['success', 'error']);
    dialogRefSpy = jasmine.createSpyObj('MatDialogRef', ['close']);

    await TestBed.configureTestingModule({
      imports: [AirportsEditModalComponent, ReactiveFormsModule, NoopAnimationsModule],
      providers: [
        FormBuilder,
        { provide: AirportService, useValue: airportServiceSpy },
        { provide: SnackbarService, useValue: snackbarSpy },
        { provide: MatDialogRef, useValue: dialogRefSpy },
        { provide: MAT_DIALOG_DATA, useValue: mockAirport }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AirportsEditModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should patch form with airport data on init', () => {
    const formValue = component.airportUpdateForm.value;
    expect(formValue.airportName).toBe(mockAirport.airportName);
    expect(formValue.city).toBe(mockAirport.city);
    expect(formValue.country).toBe(mockAirport.country);
  });

  it('should call updateAirport and close dialog on success', () => {
    airportServiceSpy.updateAirport.and.returnValue(of({}));

    component.airportUpdateForm.patchValue({
      airportName: 'Updated JFK',
      city: 'New York City',
      country: 'USA',
      u3digitCode: 'JFK'
    });

    component.updateAirport();

    expect(airportServiceSpy.updateAirport).toHaveBeenCalledWith(jasmine.objectContaining({
      id: mockAirport.airportId,
      airportName: 'Updated JFK',
      city: 'New York City',
      country: 'USA',
      u3digitCode: 'JFK'
    }));
    expect(snackbarSpy.success).toHaveBeenCalledWith('Airport Details updated successfully!');
    expect(dialogRefSpy.close).toHaveBeenCalledWith('success');
  });

  it('should show error snackbar when updateAirport fails', () => {
    airportServiceSpy.updateAirport.and.returnValue(
      throwError(() => ({ error: { key: 'ERR_UPDATE' } }))
    );

    component.airportUpdateForm.patchValue({
      airportName: 'Updated JFK',
      city: 'New York City',
      country: 'USA',
      u3digitCode: 'JFK'
    });

    component.updateAirport();

    expect(snackbarSpy.error).toHaveBeenCalledWith(
      'Airport Details were not updated successfully! ERR_UPDATE'
    );
    expect(dialogRefSpy.close).not.toHaveBeenCalled();
  });

  it('should not call updateAirport if form is invalid', () => {
    component.airportUpdateForm.patchValue({
      airportName: '',
      city: '',
      country: '',
      u3digitCode: ''
    });

    component.updateAirport();

    expect(airportServiceSpy.updateAirport).not.toHaveBeenCalled();
    expect(snackbarSpy.success).not.toHaveBeenCalled();
  });

  it('should close dialog without result', () => {
    component.close();
    expect(dialogRefSpy.close).toHaveBeenCalled();
  });
});
