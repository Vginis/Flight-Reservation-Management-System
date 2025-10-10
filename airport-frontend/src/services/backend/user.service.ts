import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

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
}