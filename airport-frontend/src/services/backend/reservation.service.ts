import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { ReservationsSearchParams } from "../../models/common.models";

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

    searchReservations(params: ReservationsSearchParams): Observable<any> {
        let httpParams = new HttpParams();
        
        httpParams = httpParams.set('size', params.size);
        httpParams = httpParams.set('index', params.index);
       
        if (params.sortDirection) httpParams = httpParams.set('sortDirection', params.sortDirection);
        return this.httpClient.get<any>(`${this.reservationsUrl}`, { params: httpParams });
    }
    
}