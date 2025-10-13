import { Injectable } from "@angular/core";
import { KeycloakService } from "keycloak-angular";
import { KeycloakUserProfile } from "../../models/user.models";

@Injectable({
    providedIn: 'root'
})
export class IdentityService {
    constructor(
        private readonly keycloakService: KeycloakService
    ){}

    getKeycloakProfileIdentity(): KeycloakUserProfile | undefined {
        const keycloakInstance = this.keycloakService.getKeycloakInstance();

        if(!keycloakInstance?.tokenParsed){
            return undefined;
        }

        return this.constructUserProfile(keycloakInstance);
    }

    private constructUserProfile(keycloakInstance: any): KeycloakUserProfile {
        return {
            id: keycloakInstance.subject!,
            username: keycloakInstance.tokenParsed['preferred_username'],
            firstName: keycloakInstance.tokenParsed['given_name'],
            lastName: keycloakInstance.tokenParsed['family_name'],
            email: keycloakInstance.tokenParsed['email'],
            roles: keycloakInstance.tokenParsed['realm_access']['roles']
        }
    }

    hasRole(role: string): boolean {
        const profile = this.getKeycloakProfileIdentity();
        return (profile!==undefined) ? profile.roles.includes(role) : false; 
    }
}