import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { SearchParams } from "../../models/common.models";

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

    searchFlightsTable(params: SearchParams): Observable<any>{
        let httpParams = new HttpParams();

        if (params.searchField !== undefined) httpParams = httpParams.set('searchField', params.searchField);
        if (params.searchValue !== undefined) httpParams = httpParams.set('searchValue', params.searchValue);
        
        httpParams = httpParams.set('size', params.size);
        httpParams = httpParams.set('index', params.index);
       
        if (params.sortDirection) httpParams = httpParams.set('sortDirection', params.sortDirection);
        if (params.sortBy) httpParams = httpParams.set('sortBy', params.sortBy);
        return this.httpClient.get<any>(`${this.flightUrl}`, { params: httpParams });
    }
}