import { Injectable } from "@angular/core";
import { FlightSeatUpdate } from "../../models/flight.models";
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { Observable, Subject } from "rxjs";
import { environment } from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class FlightSeatLayoutService {

    private socket?: WebSocketSubject<FlightSeatUpdate>;
    private readonly connection$ = new Subject<'OPEN' | 'CLOSED' | 'ERROR'>();

    connect(flightUUID: string): Observable<FlightSeatUpdate> {
        this.socket = webSocket<FlightSeatUpdate>({
            url: `${environment.backend.ws_url}/flight-seat-layout/${flightUUID}`,
            deserializer: msg => {
                console.log(msg);
                return msg.data
            },
            serializer: value => JSON.stringify(value),
            openObserver: {
                next: () => {
                console.log('✅ WebSocket connected');
                this.connection$.next('OPEN');
                }
            },
            closeObserver: {
                next: () => {
                console.log('❌ WebSocket disconnected');
                this.connection$.next('CLOSED');
                }
            }
        });

        return this.socket.asObservable();
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