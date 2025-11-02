import { Injectable } from "@angular/core";
import { SearchParams } from "../../models/common.models";
import { Observable } from "rxjs";
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class AircraftService {
    aircraftsUrl = environment.backend.url + '/aircrafts';
    constructor(private readonly httpClient: HttpClient){}
    
    searchAircrafts(params: SearchParams): Observable<any> {
        let httpParams = new HttpParams();

        if (params.searchField !== undefined) httpParams = httpParams.set('searchField', params.searchField);
        if (params.searchValue !== undefined) httpParams = httpParams.set('searchValue', params.searchValue);
        
        httpParams = httpParams.set('size', params.size);
        httpParams = httpParams.set('index', params.index);
       
        if (params.sortDirection) httpParams = httpParams.set('sortDirection', params.sortDirection);
        if (params.sortBy) httpParams = httpParams.set('sortBy', params.sortBy);
        return this.httpClient.get<any>(`${this.aircraftsUrl}`, { params: httpParams });
    }

    smartSearchAircrafts(aircraftName: string): Observable<any> {
        let httpParams = new HttpParams();
        httpParams = httpParams.set("aircraftName", aircraftName);
        return this.httpClient.get<any>(`${this.aircraftsUrl}/smart-search`, { params: httpParams });
    }
    
    createAircraft(payload: any): Observable<any> {
        return this.httpClient.post<any>(`${this.aircraftsUrl}`, payload);
    }

    updateAircraft(payload: any, id: number): Observable<any> {
        return this.httpClient.put<any>(`${this.aircraftsUrl}/${id}`, payload);
    }

    deleteAircraft(id: number): Observable<any> {
        return this.httpClient.delete<any>(`${this.aircraftsUrl}/${id}`);
    }
}