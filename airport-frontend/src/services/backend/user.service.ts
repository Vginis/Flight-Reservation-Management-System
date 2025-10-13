import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { SearchParams } from "../../models/common.models";

@Injectable({
    providedIn: 'root'
})
export class UserService {
    private readonly baseUrl = environment.backend.url;
    private readonly usersUrl = this.baseUrl + "/users";

    constructor(private readonly httpClient: HttpClient){}

    getUserProfile(): Observable<any> {
        return this.httpClient.get<any>(`${this.usersUrl}/user-profile`);
    }

    updateUserProfile(payload: any): Observable<any> {
        return this.httpClient.put(`${this.usersUrl}`, payload);
    }

    resetPassword(payload: any): Observable<any> {
        return this.httpClient.put(`${this.usersUrl}/password-reset`,payload);
    }

    searchUsers(params: SearchParams): Observable<any> {
        let httpParams = new HttpParams();

        if (params.searchField !== undefined) httpParams = httpParams.set('searchField', params.searchField);
        if (params.searchValue !== undefined) httpParams = httpParams.set('searchValue', params.searchValue);
        
        httpParams = httpParams.set('size', params.size);
        httpParams = httpParams.set('index', params.index);
       
        if (params.sortDirection) httpParams = httpParams.set('sortDirection', params.sortDirection);
        if (params.sortBy) httpParams = httpParams.set('sortBy', params.sortBy);
        return this.httpClient.get<any>(`${this.usersUrl}`, { params: httpParams });
    }
}