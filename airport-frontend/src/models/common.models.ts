import { AirportRepresentation } from "./airport.models";

export interface SearchParams {
    searchField: string,
    searchValue: string,
    size: number,
    index: number,
    sortBy: string;
    sortDirection: string;
}

export interface ReservationsSearchParams {
    size: number,
    index: number,
    sortDirection: string;
}

export interface FlightSearchParams extends SearchParams {
    departureDate: string;
    arrivalDate: string;
    departureAirport: number | null;
    arrivalAirport: number | null;
}

export interface FlightSearchHomePageParams {
    departureDate: string;
    arrivalDate: string;
    departureAirport: AirportRepresentation | null;
    arrivalAirport: AirportRepresentation | null;
    size: number,
    index: number,
}

export interface KeyValuePairs {
    key: string;
    value: string;
}