import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FlightSeatLayoutService } from '../../../../services/backend/flightseatlayout.service';
import { FlightService } from '../../../../services/backend/flight.service';
import { IdentityService } from '../../../../services/keycloak/identity.service';
import { of, Subject } from 'rxjs';
import { FlightSeatLayoutRepresentation, FlightRepresentation } from '../../../../models/flight.models';
import { SeatSelectionComponent } from '../seat-selection/seat-selection.component';

describe('SeatSelectionComponent', () => {
  let component: SeatSelectionComponent;
  let fixture: ComponentFixture<SeatSelectionComponent>;
  let flightServiceMock: jasmine.SpyObj<FlightService>;
  let flightSeatLayoutServiceMock: jasmine.SpyObj<FlightSeatLayoutService>;
  let identityServiceMock: jasmine.SpyObj<IdentityService>;

  beforeEach(async () => {
    const flightServiceSpy = jasmine.createSpyObj('FlightService', ['getFlightSeatLayoutByUUID']);
    const flightSeatLayoutSpy = jasmine.createSpyObj('FlightSeatLayoutService', ['connect', 'getConnectionStatus', 'selectSeat', 'deselectSeat', 'disconnect'], {
      selectedSeats$: new Subject<Set<string>>()
    });
    const identitySpy = jasmine.createSpyObj('IdentityService', ['getKeycloakProfileIdentity']);

    await TestBed.configureTestingModule({
      imports: [SeatSelectionComponent],
      providers: [
        { provide: FlightService, useValue: flightServiceSpy },
        { provide: FlightSeatLayoutService, useValue: flightSeatLayoutSpy },
        { provide: IdentityService, useValue: identitySpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SeatSelectionComponent);
    component = fixture.componentInstance;

    flightServiceMock = TestBed.inject(FlightService) as jasmine.SpyObj<FlightService>;
    flightSeatLayoutServiceMock = TestBed.inject(FlightSeatLayoutService) as jasmine.SpyObj<FlightSeatLayoutService>;
    identityServiceMock = TestBed.inject(IdentityService) as jasmine.SpyObj<IdentityService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch seat layout and load selected/booked seats on init', () => {
    component.flightUUID = 'test-uuid';
    identityServiceMock.getKeycloakProfileIdentity.and.returnValue({id: '1',
        username: 'jdoe',
        firstName: 'john',
        lastName: 'doe',
        email: 'email@email.com',
        roles: []});

    const flightInfo: FlightRepresentation = {
      id: 1,
      flightUUID: 'test-uuid',
      flightStatus: 'status',
      airlineU2DigitCode: 'AA',
      aircraft: {
        id: 1,
        aircraftName: 'aircraft-name',
        aircraftCapacity: 15,
        aircraftColumns: 5,
        aircraftRows: 3,
        airline2DigitCode: 'AA'
      },  
      flightNumber: 'XY123',
      departureAirport: { 
        airportId: 1,
        airportName: 'name',
        country: 'Greece',
        city: 'CityA', 
        u3digitCode: 'AAA' },
      arrivalAirport: {   
        airportId: 2,
        airportName: 'name2',
        country: 'Greece',
        city: 'CityB', 
        u3digitCode: 'BBB' },
      departureTime: '',
      arrivalTime: ''
    };
    const seatLayout: FlightSeatLayoutRepresentation = {
      flightInformation: flightInfo,
      rows: 2,
      columns: 4,
      flightSeatRepresentationList: [
        { rowIndex: 0, columnIndex: 0, seatReservationState: 'LOCKED', lastUpdatedBy: 'user1' },
        { rowIndex: 0, columnIndex: 1, seatReservationState: 'BOOKED', lastUpdatedBy: 'user2' }
      ]
    };
    flightServiceMock.getFlightSeatLayoutByUUID.and.returnValue(of(seatLayout));
    flightSeatLayoutServiceMock.connect.and.returnValue(of());

    fixture.detectChanges();

    expect(component.seatRows.length).toBe(2);
    expect(component.leftSeatColumns.length).toBe(2);
    expect(component.rightSeatColumns.length).toBe(2);
    expect(component.selectedSeats.has('0-0')).toBeTrue();
    expect(component.bookedSeats.has('0-1')).toBeTrue();
  });

  it('should toggle seat selection on click', () => {
    component.flightUUID = 'test-uuid';
    component.selectedSeats = new Set();

    flightSeatLayoutServiceMock.selectSeat.and.callFake((seatId: string, pl: any) => {
      component.selectedSeats.add(seatId);
    });
    flightSeatLayoutServiceMock.deselectSeat.and.callFake((seatId: string, pl: any) => {
      component.selectedSeats.delete(seatId);
    });

    spyOn(component.selectedSeatsChange, 'emit');

    component.onSeatClick(0, 0);
    expect(component.selectedSeats.has('0-0')).toBeTrue();
    expect(component.selectedSeatsChange.emit).toHaveBeenCalledWith(new Set(['0-0']));

    component.onSeatClick(0, 0);
    expect(component.selectedSeats.has('0-0')).toBeFalse();
  });

  it('should return correct booked/locked states', () => {
    component.selectedSeats = new Set(['0-0']);
    component.bookedSeats = new Set(['0-1']);

    expect(component.isLocked(0, 0)).toBeTrue();
    expect(component.isLocked(0, 1)).toBeFalse();
    expect(component.isBooked(0, 1)).toBeTrue();
    expect(component.isBooked(0, 0)).toBeFalse();
  });

  it('should generate seat ID correctly', () => {
    expect(component.getSeatId(2, 3)).toBe('2-3');
  });

  it('should cleanup subscription and disconnect on destroy', () => {
    const sub = flightSeatLayoutServiceMock.connect('uuid').subscribe();
    component.sub = sub;
    component.ngOnDestroy();
    expect(flightSeatLayoutServiceMock.disconnect).toHaveBeenCalled();
  });

  it('should compute right side index correctly', () => {
    component.leftSeatColumns = Array.from({ length: 4 });
    expect(component.getRightSideIndex(2)).toBe(6);
  });

  it('should display flight header and additional info', () => {

    component.flightInformation = {
      id: 1,
      flightUUID: 'test-uuid',
      flightStatus: 'status',
      airlineU2DigitCode: 'AA',
      aircraft: {
        id: 1,
        aircraftName: 'aircraft-name',
        aircraftCapacity: 15,
        aircraftColumns: 5,
        aircraftRows: 3,
        airline2DigitCode: 'AA'
      },  
      flightNumber: 'XY123',
      departureAirport: { 
        airportId: 1,
        airportName: 'name',
        country: 'Greece',
        city: 'CityA', 
        u3digitCode: 'AAA' },
      arrivalAirport: {   
        airportId: 2,
        airportName: 'name2',
        country: 'Greece',
        city: 'CityB', 
        u3digitCode: 'BBB' },
      departureTime: '',
      arrivalTime: ''
    };
    expect(component.displayBookingHeaderInformation()).toContain('XY123');
    expect(component.displayAdditionalFlightInfo()).toContain('Departure');
  });
});
