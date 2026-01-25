import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { FlightsTableComponent } from './flights-table.component';
import { FlightService } from '../../../../../services/backend/flight.service';
import { MatDialog } from '@angular/material/dialog';
import { of } from 'rxjs';
import { FlightRepresentation } from '../../../../../models/flight.models';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { CommonUtils } from '../../../../../utils/common.util';
import { AirportService } from '../../../../../services/backend/airport.service';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('FlightsTableComponent', () => {
  let component: FlightsTableComponent;
  let fixture: ComponentFixture<FlightsTableComponent>;
  let flightService: jasmine.SpyObj<FlightService>;
  let airportService: jasmine.SpyObj<AirportService>
  let dialog: jasmine.SpyObj<MatDialog>;

  beforeEach(async () => {
    flightService = jasmine.createSpyObj('FlightService', ['searchFlightsTable']);
    airportService = jasmine.createSpyObj('AirportService', ['smartSearchAirports']);
    dialog = jasmine.createSpyObj('MatDialog', ['open']);

    flightService.searchFlightsTable.and.returnValue(of({
      results: [{ 
        flightNumber: 'LH123', 
        flightStatus: 'SCHEDULED',
        departureAirport: { u3digitCode: 'JFK' } ,
        arrivalAirport: { u3digitCode: 'ATH' } 
    }] as FlightRepresentation[],
      total: 1
    }));
    airportService.smartSearchAirports.and.returnValue(of([
        {
            airportId: 1,
            airportName: 'New York Airport',
            city: 'New York',
            country: 'USA',
            u3digitCode: 'JFK'
        }
    ]));

    await TestBed.configureTestingModule({
      imports: [FlightsTableComponent, NoopAnimationsModule],
      providers: [
        { provide: FlightService, useValue: flightService },
        { provide: AirportService, useValue: airportService},
        { provide: MatDialog, useValue: dialog }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FlightsTableComponent);
    component = fixture.componentInstance;

    component.paginator = { pageIndex: 0, pageSize: 10, length: 0, page: of({}) } as any as MatPaginator;
    component.sort = { sortChange: of({}), active: 'flightNumber', direction: 'asc' } as any as MatSort;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.displayedColumns.length).toBeGreaterThan(0);
  });

  it('should load flights table on init', () => {
    component.loadFlightsTable();
    expect(flightService.searchFlightsTable).toHaveBeenCalled();
    expect(component.dataSource.data.length).toBe(1);
    expect(component.paginator.length).toBe(1);
  });

  it('resetSearchParams should reset params and reload table', () => {
    const loadSpy = spyOn(component, 'loadFlightsTable');
    component.resetSearchParams();
    expect(component.params.index).toBe(0);
    expect(component.params.sortBy).toBe('flightNumber');
    expect(loadSpy).toHaveBeenCalled();
  });

  it('handleFormSubmit should update params and reload table', () => {
    const loadSpy = spyOn(component, 'loadFlightsTable');
    const formData = {
      filterBy: 'flightNumber',
      filterValue: 'LH123',
      departureAirport: { airportId: 1 },
      arrivalAirport: { airportId: 2 },
      departureDate: new Date('2025-01-01'),
      arrivalDate: new Date('2025-01-02')
    };

    spyOn<any>(CommonUtils, 'formatDateForDateTimePattern').and.callFake((d: any) => d.toISOString());

    component.handleFormSubmit(formData);

    expect(component.params.searchField).toBe('flightNumber');
    expect(component.params.searchValue).toBe('LH123');
    expect(component.params.departureAirport).toBe(1);
    expect(component.params.arrivalAirport).toBe(2);
    expect(component.params.departureDate).toBe(formData.departureDate.toISOString());
    expect(component.params.arrivalDate).toBe(formData.arrivalDate.toISOString());
    expect(loadSpy).toHaveBeenCalled();
  });

  it('flightIsNotInFinalStatus should return true/false correctly', () => {
    expect(component.flightIsNotInFinalStatus({ flightStatus: 'SCHEDULED' } as FlightRepresentation)).toBeTrue();
    expect(component.flightIsNotInFinalStatus({ flightStatus: 'CANCELLED' } as FlightRepresentation)).toBeFalse();
    expect(component.flightIsNotInFinalStatus({ flightStatus: 'ARRIVED' } as FlightRepresentation)).toBeFalse();
  });

  it('openCreateModal should open dialog and reload table on success', fakeAsync(() => {
    const afterClosedSpy = jasmine.createSpyObj({ subscribe: (fn: any) => fn('success') });
    dialog.open.and.returnValue({ afterClosed: () => afterClosedSpy } as any);

    component.openCreateModal();
    tick();

    expect(dialog.open).toHaveBeenCalled();
  }));

  it('editFlight should open dialog with data and reload table', fakeAsync(() => {
    const afterClosed = of('success');
    dialog.open.and.returnValue({ afterClosed: () => afterClosed } as any);
    const flightData = { flightNumber: 'LH123' };

    const loadSpy = spyOn(component, 'loadFlightsTable');
    component.editFlight(flightData);
    tick();

    expect(dialog.open).toHaveBeenCalledWith(jasmine.any(Function), { data: flightData });
    expect(loadSpy).toHaveBeenCalled();
  }));

  it('cancelFlight should open dialog with data and reload table', fakeAsync(() => {
    const afterClosed = of('success');
    dialog.open.and.returnValue({ afterClosed: () => afterClosed } as any);
    const flightData = { flightNumber: 'LH123' };

    const loadSpy = spyOn(component, 'loadFlightsTable');
    component.cancelFlight(flightData);
    tick();

    expect(dialog.open).toHaveBeenCalledWith(jasmine.any(Function), { data: flightData });
    expect(loadSpy).toHaveBeenCalled();
  }));

  it('updateFlightStatus should open dialog with data and reload table', fakeAsync(() => {
    const afterClosed = of('success');
    dialog.open.and.returnValue({ afterClosed: () => afterClosed } as any);
    const flightData = { flightNumber: 'LH123' };

    const loadSpy = spyOn(component, 'loadFlightsTable');
    component.updateFlightStatus(flightData);
    tick();

    expect(dialog.open).toHaveBeenCalledWith(jasmine.any(Function), { data: flightData });
    expect(loadSpy).toHaveBeenCalled();
  }));
});
