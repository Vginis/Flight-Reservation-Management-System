import { Injectable } from "@angular/core";
import { KeycloakService } from "keycloak-angular";

@Injectable({
    providedIn: 'root'
})
export class IdentityService {
    constructor(
        private readonly keycloakService: KeycloakService
    ){}

    
}