import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FlightsCreateModalComponent } from './flights-create-modal.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { of, throwError } from 'rxjs';
import { FlightService } from '../../../../../services/backend/flight.service';
import { AirportService } from '../../../../../services/backend/airport.service';
import { AircraftService } from '../../../../../services/backend/aircraft.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('FlightsCreateModalComponent', () => {
  let component: FlightsCreateModalComponent;
  let fixture: ComponentFixture<FlightsCreateModalComponent>;

  let flightService: jasmine.SpyObj<FlightService>;
  let airportService: jasmine.SpyObj<AirportService>;
  let aircraftService: jasmine.SpyObj<AircraftService>;
  let snackbar: jasmine.SpyObj<SnackbarService>;
  let dialogRef: jasmine.SpyObj<MatDialogRef<FlightsCreateModalComponent>>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        FlightsCreateModalComponent,
        ReactiveFormsModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: null },
        {
          provide: FlightService,
          useValue: jasmine.createSpyObj('FlightService', ['createFlight', 'updateFlight'])
        },
        {
          provide: AirportService,
          useValue: jasmine.createSpyObj('AirportService', ['smartSearchAirports'])
        },
        {
          provide: AircraftService,
          useValue: jasmine.createSpyObj('AircraftService', ['smartSearchAircrafts'])
        },
        {
          provide: SnackbarService,
          useValue: jasmine.createSpyObj('SnackbarService', ['success', 'error'])
        },
        {
          provide: MatDialogRef,
          useValue: jasmine.createSpyObj('MatDialogRef', ['close'])
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FlightsCreateModalComponent);
    component = fixture.componentInstance;

    flightService = TestBed.inject(FlightService) as jasmine.SpyObj<FlightService>;
    airportService = TestBed.inject(AirportService) as jasmine.SpyObj<AirportService>;
    aircraftService = TestBed.inject(AircraftService) as jasmine.SpyObj<AircraftService>;
    snackbar = TestBed.inject(SnackbarService) as jasmine.SpyObj<SnackbarService>;
    dialogRef = TestBed.inject(MatDialogRef) as jasmine.SpyObj<MatDialogRef<FlightsCreateModalComponent>>;

    airportService.smartSearchAirports.and.returnValue(of([]));
    aircraftService.smartSearchAircrafts.and.returnValue(of([]));

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with required controls', () => {
    const form = component.flightCreateForm;
    expect(form.contains('flightNumber')).toBeTrue();
    expect(form.contains('departureAirport')).toBeTrue();
    expect(form.contains('arrivalAirport')).toBeTrue();
    expect(form.contains('aircraft')).toBeTrue();
  });

  it('should mark form invalid when required fields are missing', () => {
    component.flightCreateForm.reset();
    component.flightCreateForm.updateValueAndValidity();
    expect(component.flightCreateForm.invalid).toBeTrue();
  });

  it('should validate that departure and arrival airports are different', () => {
    const airport = { u3digitCode: 'JFK' };

    component.flightCreateForm.patchValue({
      departureAirport: airport,
      arrivalAirport: airport
    });

    component.flightCreateForm.updateValueAndValidity();

    const arrivalCtrl = component.flightCreateForm.get('arrivalAirport');
    expect(arrivalCtrl?.hasError('sameAirport')).toBeTrue();
  });

  it('should clear sameAirport error when airports differ', () => {
    component.flightCreateForm.patchValue({
      departureAirport: { u3digitCode: 'JFK' },
      arrivalAirport: { u3digitCode: 'LAX' }
    });

    component.flightCreateForm.updateValueAndValidity();

    expect(component.flightCreateForm.get('arrivalAirport')?.errors).toBeNull();
  });

  it('should calculate default start date 6 months in the future', () => {
    component.calculateDefaultStartDateForDatePicker();
    const today = new Date();
    const expected = new Date();
    expected.setMonth(today.getMonth() + 6);

    expect(component.defaultStartDate.getMonth()).toBe(expected.getMonth());
  });

  it('should create flight when form is valid and not in edit mode', () => {
    flightService.createFlight.and.returnValue(of({}));

    component.isEditMode = false;
    const departureDate = new Date();
    departureDate.setMonth(departureDate.getMonth() + 7);

    const arrivalDate = new Date(departureDate);
    arrivalDate.setDate(arrivalDate.getDate() + 1);
    component.flightCreateForm.setValue({
        flightNumber: 'LH123',
        departureAirport: { u3digitCode: 'JFK' },
        departureDate: departureDate,
        departureTime: '10:00',
        arrivalAirport: { u3digitCode: 'LAX' },
        arrivalDate: arrivalDate,
        arrivalTime: '14:00',
        aircraft: { id: 1 }
    });

    component.flightCreateForm.updateValueAndValidity();

    component.createOrUpdateFlight();
    expect(component.flightCreateForm.valid).toBeTrue();

    expect(flightService.createFlight).toHaveBeenCalled();
    expect(snackbar.success).toHaveBeenCalled();
    expect(dialogRef.close).toHaveBeenCalledWith('success');
  });

  it('should show error snackbar when flight creation fails', () => {
    flightService.createFlight.and.returnValue(
      throwError(() => ({ error: { key: 'ERROR' } }))
    );
    const departureDate = new Date();
    departureDate.setMonth(departureDate.getMonth() + 7);

    const arrivalDate = new Date(departureDate);
    arrivalDate.setDate(arrivalDate.getDate() + 1);

    component.flightCreateForm.patchValue({
      flightNumber: 'LH123',
      departureAirport: { u3digitCode: 'JFK' },
      departureDate: departureDate,
      departureTime: '10:00',
      arrivalAirport: { u3digitCode: 'LAX' },
      arrivalDate: arrivalDate,
      arrivalTime: '14:00',
      aircraft: { id: 1 }
    });

    component.createOrUpdateFlight();

    expect(snackbar.error).toHaveBeenCalled();
  });

  it('should close dialog when close() is called', () => {
    component.close();
    expect(dialogRef.close).toHaveBeenCalled();
  });

  it('should return correct labels depending on mode', () => {
    component.isEditMode = false;
    expect(component.getLabel()).toBe('Create Flight');
    expect(component.getButtonLabel()).toBe('Create');

    component.isEditMode = true;
    expect(component.getLabel()).toBe('Update Flight');
    expect(component.getButtonLabel()).toBe('Update');
  });
});
