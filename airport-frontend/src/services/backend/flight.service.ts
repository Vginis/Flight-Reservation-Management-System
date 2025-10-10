import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class FlightService {
    private readonly baseUrl = environment.backend.url;
    private readonly flightUrl = this.baseUrl + "/flights";

    constructor(private readonly httpClient: HttpClient){}

    searchFlights(origin: string, destination: string, date: string): Observable<any> {
        const params = { origin, destination, date}
        return this.httpClient.get<any>(`${this.flightUrl}/search`, {params});
    }
}