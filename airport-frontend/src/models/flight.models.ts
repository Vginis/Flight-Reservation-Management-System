export interface FlightRepresentation {
    id: number,
    flightNumber: string,
    flightUUID: string,
    airlineU2DigitCode: string,
    departureAirport: string,
    departureTime: string,
    arrivalAirport: string,
    arrivalTime: string,
    aircraftId: number
}