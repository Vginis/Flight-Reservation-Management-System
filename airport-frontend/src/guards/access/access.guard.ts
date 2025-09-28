import { Injectable } from "@angular/core";
import { KeycloakAuthGuard, KeycloakService } from "keycloak-angular";
import { IdentityService } from "../../services/keycloak/identity.service";
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from "@angular/router";

@Injectable({
    providedIn: 'root'
})
export class AccessGuard extends KeycloakAuthGuard {
    constructor(
        private readonly identityService: IdentityService,
        protected override readonly router: Router, 
        protected readonly keycloakService: KeycloakService
    ){
        super(router, keycloakService);
    }

    public override async isAccessAllowed(route: ActivatedRouteSnapshot, 
        state: RouterStateSnapshot): Promise<boolean | UrlTree> {
        if(!this.authenticated){
            await this.keycloakService.login({
                redirectUri: window.location.origin + state.url,
                locale: 'en'
            });
        }

        return this.canActivate(route, state);
    }
}