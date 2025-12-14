import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { SearchParams } from "../../models/common.models";
import { CommonUtils } from "../../utils/common.util";

@Injectable({
    providedIn: 'root'
})
export class UserService {
    private readonly baseUrl = environment.backend.url;
    private readonly usersUrl = this.baseUrl + "/users";
    private readonly airlineAdminsUrl = this.baseUrl+"/airline-admins";
    private readonly passengersUrl = this.baseUrl+"/passengers";

    constructor(private readonly httpClient: HttpClient){}

    getUserProfile(): Observable<any> {
        return this.httpClient.get<any>(`${this.usersUrl}/user-profile`);
    }

    getPassengerPassport(username: string): Observable<any> {
        return this.httpClient.get<any>(`${this.passengersUrl}`, { params: {username: username} });
    }

    updateUserProfile(payload: any): Observable<any> {
        return this.httpClient.put(`${this.usersUrl}/user-profile`, payload);
    }

    updateUserDetails(payload: any, username: string, role: string): Observable<any> {
        const url = (role === CommonUtils.PASSENGER) ? this.passengersUrl : this.usersUrl;
        return this.httpClient.put(`${url}`, payload, { params: {username: username} });
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

    createUser(payload: any, role: string): Observable<any> {
        const url = this.resolveUrlForUserCreation(role);
        return this.httpClient.post(`${url}`, payload);
    }

    createAirlineAdministrator(formData: FormData): Observable<any> {
        return this.httpClient.post(`${this.airlineAdminsUrl}`, formData)
    }

    completePassengerRegistration(payload: any): Observable<any> {
        return this.httpClient.post(`${this.passengersUrl}/complete-registration`, payload);
    }

    deleteUser(userId: number): Observable<any> {
        return this.httpClient.delete(`${this.usersUrl}/${userId}`);
    }

    private resolveUrlForUserCreation(role: string): string {
        if(role === CommonUtils.PASSENGER){
            return this.passengersUrl;
        }
        
        return this.usersUrl;
    }
}