import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { Observable } from "rxjs";
import { HttpClient, HttpParams } from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})
export class CityCountryService {
    citiesCountriesUrl = environment.backend.url + '/cities-countries'
    constructor(private readonly httpClient: HttpClient){}

    smartSearchCountries(value: string | null): Observable<any> {
        let httpParams = new HttpParams();
        if(value!==null) {
            httpParams = httpParams.set('value', value);
        }
        return this.httpClient.get<any>(`${this.citiesCountriesUrl}/smart-search/country`, { params: httpParams });
    }

    smartSearchCities(value: string | null, country: string | null | undefined): Observable<any> {
        let httpParams = new HttpParams();
        if(value!==null) {
            httpParams = httpParams.set('value', value);
        }
        if(country!==null && country !==undefined) {
            httpParams = httpParams.set('country', country)
        }
        return this.httpClient.get<any>(`${this.citiesCountriesUrl}/smart-search/city`, { params: httpParams });
    }
}