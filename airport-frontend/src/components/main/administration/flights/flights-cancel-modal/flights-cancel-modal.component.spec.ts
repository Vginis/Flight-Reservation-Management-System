import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FlightsCancelModalComponent } from './flights-cancel-modal.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FlightService } from '../../../../../services/backend/flight.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { of, throwError } from 'rxjs';
import { FlightRepresentation } from '../../../../../models/flight.models';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('FlightsCancelModalComponent', () => {
  let component: FlightsCancelModalComponent;
  let fixture: ComponentFixture<FlightsCancelModalComponent>;

  let flightServiceSpy: jasmine.SpyObj<FlightService>;
  let snackbarSpy: jasmine.SpyObj<SnackbarService>;
  let dialogRefSpy: jasmine.SpyObj<MatDialogRef<FlightsCancelModalComponent>>;

  const mockFlight: FlightRepresentation = {
    id: 42,
    flightStatus: 'SCHEDULED'
  } as FlightRepresentation;

  beforeEach(async () => {
    flightServiceSpy = jasmine.createSpyObj('FlightService', ['updateFlightStatus']);
    snackbarSpy = jasmine.createSpyObj('SnackbarService', ['success', 'error']);
    dialogRefSpy = jasmine.createSpyObj('MatDialogRef', ['close']);

    await TestBed.configureTestingModule({
      imports: [
        FlightsCancelModalComponent,
        NoopAnimationsModule
      ],
      providers: [
        { provide: FlightService, useValue: flightServiceSpy },
        { provide: SnackbarService, useValue: snackbarSpy },
        { provide: MatDialogRef, useValue: dialogRefSpy },
        { provide: MAT_DIALOG_DATA, useValue: mockFlight }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FlightsCancelModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should cancel flight successfully', () => {
    flightServiceSpy.updateFlightStatus.and.returnValue(of({}));

    component.cancelFlight();

    expect(flightServiceSpy.updateFlightStatus)
      .toHaveBeenCalledWith(mockFlight.id, 'CANCELLED');

    expect(snackbarSpy.success)
      .toHaveBeenCalledWith('Flight cancelled successfully');

    expect(dialogRefSpy.close)
      .toHaveBeenCalledWith('success');
  });

  it('should show error snackbar when cancel fails', () => {
    flightServiceSpy.updateFlightStatus.and.returnValue(
      throwError(() => ({ error: { key: 'ERR_CANCEL' } }))
    );

    component.cancelFlight();

    expect(snackbarSpy.error)
      .toHaveBeenCalledWith('Flight was not cancelled:ERR_CANCEL');

    expect(dialogRefSpy.close).not.toHaveBeenCalled();
  });

  it('should close dialog without result', () => {
    component.close();
    expect(dialogRefSpy.close).toHaveBeenCalled();
  });
});