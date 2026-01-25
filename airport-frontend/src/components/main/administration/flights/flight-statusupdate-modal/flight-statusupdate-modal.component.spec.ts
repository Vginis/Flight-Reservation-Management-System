import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FlightStatusUpdateModalComponent } from './flight-statusupdate-modal.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FlightService } from '../../../../../services/backend/flight.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { of, throwError } from 'rxjs';
import { FlightRepresentation } from '../../../../../models/flight.models';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('FlightStatusUpdateModalComponent', () => {
  let component: FlightStatusUpdateModalComponent;
  let fixture: ComponentFixture<FlightStatusUpdateModalComponent>;

  let flightServiceSpy: jasmine.SpyObj<FlightService>;
  let snackbarSpy: jasmine.SpyObj<SnackbarService>;
  let dialogRefSpy: jasmine.SpyObj<MatDialogRef<FlightStatusUpdateModalComponent>>;

  const mockFlight: FlightRepresentation = {
    id: 1,
    flightStatus: 'SCHEDULED'
  } as FlightRepresentation;

  beforeEach(async () => {
    flightServiceSpy = jasmine.createSpyObj('FlightService', ['updateFlightStatus']);
    snackbarSpy = jasmine.createSpyObj('SnackbarService', ['success', 'error']);
    dialogRefSpy = jasmine.createSpyObj('MatDialogRef', ['close']);

    await TestBed.configureTestingModule({
      imports: [
        FlightStatusUpdateModalComponent,
        NoopAnimationsModule
      ],
      providers: [
        { provide: FlightService, useValue: flightServiceSpy },
        { provide: SnackbarService, useValue: snackbarSpy },
        { provide: MatDialogRef, useValue: dialogRefSpy },
        { provide: MAT_DIALOG_DATA, useValue: mockFlight }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FlightStatusUpdateModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize selected status from injected flight data', () => {
    expect(component.selectedStatus).toBe('SCHEDULED');
    expect(component.inititalSelectedStatus).toBe('SCHEDULED');
  });

  it('should filter available statuses based on allowed transitions', () => {
    component.selectedStatus = 'SCHEDULED';
    component.filterAvailableStatuses();

    const keys = component.availableStatuses.map(s => s.key);
    expect(keys).toContain('SCHEDULED');
    expect(keys).toContain('DELAYED');
    expect(keys).toContain('IN_FLIGHT');
    expect(keys).not.toContain('ARRIVED');
  });

  it('should detect when the same status is selected', () => {
    component.selectedStatus = 'SCHEDULED';
    expect(component.hasSelectedTheSameOption()).toBeTrue();

    component.selectedStatus = 'DELAYED';
    expect(component.hasSelectedTheSameOption()).toBeFalse();
  });

  it('should update flight status successfully', () => {
    flightServiceSpy.updateFlightStatus.and.returnValue(of({}));

    component.selectedStatus = 'DELAYED';
    component.updateFlightStatus();

    expect(flightServiceSpy.updateFlightStatus)
      .toHaveBeenCalledWith(mockFlight.id, 'DELAYED');

    expect(snackbarSpy.success)
      .toHaveBeenCalledWith('Flight status updated successfully');

    expect(dialogRefSpy.close).toHaveBeenCalledWith('success');
  });

  it('should show error snackbar when update fails', () => {
    flightServiceSpy.updateFlightStatus.and.returnValue(
      throwError(() => ({ error: { key: 'ERR_UPDATE' } }))
    );

    component.selectedStatus = 'DELAYED';
    component.updateFlightStatus();

    expect(snackbarSpy.error)
      .toHaveBeenCalledWith('Flight Status was not updated:ERR_UPDATE');

    expect(dialogRefSpy.close).not.toHaveBeenCalled();
  });

  it('should close dialog without result', () => {
    component.close();
    expect(dialogRefSpy.close).toHaveBeenCalled();
  });
});