import { AircraftRepresentation } from "./aircraft.model";
import { AirportRepresentation } from "./airport.models";

export interface FlightRepresentation {
    id: number,
    flightNumber: string,
    flightUUID: string,
    flightStatus: string,
    airlineU2DigitCode: string,
    departureAirport: AirportRepresentation,
    departureTime: string,
    arrivalAirport: AirportRepresentation,
    arrivalTime: string,
    aircraft: AircraftRepresentation
}

export type SeatState = 'AVAILABLE' | 'LOCKED' | 'BOOKED';

export interface FlightSeatUpdate {
  flightUUID: string;
  rowIndex: number;
  columnIndex: number;
  seatReservationState: SeatState;
}

export interface FlightSeatRepresentation {
  rowIndex: number;
  columnIndex: number;
  seatReservationState: SeatState;
  lastUpdatedBy: string;
}

export interface FlightSeatLayoutRepresentation {
  rows: number,
  columns: number,
  flightSeatRepresentationList: FlightSeatRepresentation[],
  flightInformation: FlightRepresentation
}