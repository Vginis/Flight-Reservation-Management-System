import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { FlightsFilteringComponent } from './flights-filtering.component';
import { AirportService } from '../../../../../services/backend/airport.service';
import { of } from 'rxjs';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('FlightsFilteringComponent', () => {
  let component: FlightsFilteringComponent;
  let fixture: ComponentFixture<FlightsFilteringComponent>;
  let airportService: jasmine.SpyObj<AirportService>;

  beforeEach(async () => {
    const airportSpy = jasmine.createSpyObj('AirportService', ['smartSearchAirports']);

    await TestBed.configureTestingModule({
      imports: [
        FlightsFilteringComponent,
        NoopAnimationsModule
      ],
      providers: [
        { provide: AirportService, useValue: airportSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FlightsFilteringComponent);
    component = fixture.componentInstance;
    airportService = TestBed.inject(AirportService) as jasmine.SpyObj<AirportService>;

    airportService.smartSearchAirports.and.returnValue(of([]));

    fixture.detectChanges();
  });

  it('should create the component and initialize the form', () => {
    expect(component).toBeTruthy();
    expect(component.flightsFilterForm).toBeTruthy();
    const form = component.flightsFilterForm;
    expect(form.contains('filterBy')).toBeTrue();
    expect(form.contains('departureAirport')).toBeTrue();
    expect(form.contains('arrivalAirport')).toBeTrue();
  });

  it('should update airports list when departureAirport value changes', fakeAsync(() => {
    airportService.smartSearchAirports.and.returnValue(of([{ city: 'NYC', u3digitCode: 'JFK' }]));
    component.flightsFilterForm.get('departureAirport')!.setValue('New');
    tick(300); // debounce time
    fixture.detectChanges();
    expect(airportService.smartSearchAirports).toHaveBeenCalledWith('New');
    expect(component.airports.length).toBe(1);
    expect(component.airports[0].city).toBe('NYC');
  }));

  it('should update airports list when arrivalAirport value changes', fakeAsync(() => {
    airportService.smartSearchAirports.and.returnValue(of([{ city: 'LAX', u3digitCode: 'LAX' }]));
    component.flightsFilterForm.get('arrivalAirport')!.setValue('Los');
    tick(300);
    fixture.detectChanges();
    expect(airportService.smartSearchAirports).toHaveBeenCalledWith('Los');
    expect(component.airports.length).toBe(1);
    expect(component.airports[0].city).toBe('LAX');
  }));

  it('disablePastDates should return true for today or future dates', () => {
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(today.getDate() + 1);
    const yesterday = new Date(today);
    yesterday.setDate(today.getDate() - 1);

    expect(component.disablePastDates(today)).toBeTrue();
    expect(component.disablePastDates(tomorrow)).toBeTrue();
    expect(component.disablePastDates(yesterday)).toBeFalse();
    expect(component.disablePastDates(null)).toBeFalse();
  });

  it('validateArrivalDates should enforce arrival >= departure', () => {
    const departureDate = new Date('2025-01-01');
    const arrivalDateValid = new Date('2025-01-02');
    const arrivalDateInvalid = new Date('2024-12-31');

    component.flightsFilterForm.patchValue({ departureDate });

    expect(component.validateArrivalDates(arrivalDateValid)).toBeTrue();
    expect(component.validateArrivalDates(arrivalDateInvalid)).toBeFalse();
    expect(component.validateArrivalDates(null)).toBeFalse();
  });

  it('onDepartingSelected should reset arrivalDate if earlier than departure', () => {
    const arrDate = new Date('2025-01-06');

    component.flightsFilterForm.patchValue({ arrivalDate: arrDate });
    component.onDepartingSelected({ value: new Date('2025-01-07') });

    expect(component.flightsFilterForm.value.arrivalDate).toBe('');
  });

  it('displayAirport should return formatted string', () => {
    const airport = { city: 'Paris', u3digitCode: 'CDG' };
    expect(component.displayAirport(airport)).toBe('Paris (CDG)');
    expect(component.displayAirport(null)).toBe('');
  });

  it('onSubmit should emit form value', () => {
    spyOn(component.filterFormSubmission, 'emit');
    const value = { filterBy: 'flightNumber', filterValue: 'LH123' };
    component.flightsFilterForm.patchValue(value);
    component.onSubmit();
    expect(component.filterFormSubmission.emit).toHaveBeenCalledWith(component.flightsFilterForm.value);
  });

  it('resetFilterForm should clear form and emit', () => {
    spyOn(component.filterFormSubmission, 'emit');
    component.flightsFilterForm.patchValue({
      filterBy: 'flightNumber',
      filterValue: 'LH123',
      departureAirport: 'JFK',
      departureDate: new Date(),
      arrivalAirport: 'LAX',
      arrivalDate: new Date()
    });

    component.resetFilterForm();

    const form = component.flightsFilterForm.value;
    expect(form.filterBy).toBe('');
    expect(form.filterValue).toBe('');
    expect(form.departureAirport).toBe('');
    expect(form.arrivalAirport).toBe('');
    expect(component.filterFormSubmission.emit).toHaveBeenCalledWith(component.flightsFilterForm.value);
  });
});
