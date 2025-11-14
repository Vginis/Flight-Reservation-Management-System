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