import { Injectable } from "@angular/core";
import { FlightSeatUpdate } from "../../models/flight.models";
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { map, Observable, Subject, switchMap } from "rxjs";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})
export class FlightSeatLayoutService {

    private socket?: WebSocketSubject<FlightSeatUpdate>;
    private readonly connection$ = new Subject<'OPEN' | 'CLOSED' | 'ERROR'>();
    
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
}