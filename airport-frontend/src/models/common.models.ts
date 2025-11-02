export interface SearchParams {
    searchField: string,
    searchValue: string,
    size: number,
    index: number,
    sortBy: string;
    sortDirection: string;
}

export interface FlightSearchParams extends SearchParams {
    departureDate: string;
    arrivalDate: string;
    departureAirport: number | null;
    arrivalAirport: number | null;
}