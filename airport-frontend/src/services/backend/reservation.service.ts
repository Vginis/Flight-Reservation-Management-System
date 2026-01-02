import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ReservationService {
    private readonly baseUrl = environment.backend.url;
    private readonly reservationsUrl = this.baseUrl + "/reservations";

    constructor(private readonly httpClient: HttpClient){}

    createReservation(payload: any): Observable<any> {
        return this.httpClient.post<any>(`${this.reservationsUrl}`, payload);
    }
    
}