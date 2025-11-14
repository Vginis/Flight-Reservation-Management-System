import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { SearchParams } from "../../models/common.models";
import { Observable } from "rxjs";
import { HttpClient, HttpParams } from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})
export class AirlineService {
    private readonly baseUrl = environment.backend.url;
    private readonly airlinesUrl = this.baseUrl + "/airlines";

    constructor(private readonly httpClient: HttpClient){}

    searchAirlines(params: SearchParams): Observable<any> {
        let httpParams = new HttpParams();

        if (params.searchField !== undefined) httpParams = httpParams.set('searchField', params.searchField);
        if (params.searchValue !== undefined) httpParams = httpParams.set('searchValue', params.searchValue);

        httpParams = httpParams.set('size', params.size);
        httpParams = httpParams.set('index', params.index);

        if (params.sortDirection) httpParams = httpParams.set('sortDirection', params.sortDirection);
        if (params.sortBy) httpParams = httpParams.set('sortBy', params.sortBy);
        return this.httpClient.get<any>(`${this.airlinesUrl}`, { params: httpParams });
    }

    getAirlineLogos(airlineCodes: Set<string>): Observable<any> {
        return this.httpClient.post<any>(`${this.airlinesUrl}/logos`, [...airlineCodes]);
    }
}
