import { Injectable } from "@angular/core";
import { FlightSeatUpdate } from "../../models/flight.models";
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { BehaviorSubject, map, Observable, Subject, switchMap } from "rxjs";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})
export class FlightSeatLayoutService {

    private socket?: WebSocketSubject<FlightSeatUpdate>;
    private readonly connection$ = new Subject<'OPEN' | 'CLOSED' | 'ERROR'>();
    private readonly selectedSeatsSubject = new BehaviorSubject<Set<string>>(new Set());

    selectedSeats$ = this.selectedSeatsSubject.asObservable();

    constructor(
        private readonly httpClient: HttpClient
    ) {
    }

    createNewSession(flightUUID: string) : Observable<string> {
        return this.httpClient.post<{ wsSessionId: string }>(
            `${environment.backend.url}/ws-session/${flightUUID}`, {}
        ).pipe(map(res => res.wsSessionId));
    }

    connect(flightUUID: string): Observable<FlightSeatUpdate> {
        return this.createNewSession(flightUUID).pipe(
            switchMap(sessionId => {
            this.socket = webSocket<FlightSeatUpdate>({
                url: `${environment.backend.ws_url}/flight-seat-layout/${flightUUID}?sessionId=${sessionId}`,
                serializer: value => JSON.stringify(value),
                deserializer: msg => msg.data
            });
            return this.socket.asObservable();
            })
        );
    }

    sendSeatUpdate(update: FlightSeatUpdate): void {
        this.socket?.next(update);
    }

    getConnectionStatus() {
        return this.connection$.asObservable();
    }

    disconnect(): void {
        this.socket?.complete();
    }

    selectSeat(seatId: string, update: FlightSeatUpdate): void {
        const next = new Set(this.selectedSeatsSubject.value);
        next.add(seatId);
        this.selectedSeatsSubject.next(next);
        this.sendSeatUpdate(update);
    }

    deselectSeat(seatId: string, update: FlightSeatUpdate): void {
        const next = new Set(this.selectedSeatsSubject.value);
        next.delete(seatId);
        this.selectedSeatsSubject.next(next);
        this.sendSeatUpdate(update);
    }

}