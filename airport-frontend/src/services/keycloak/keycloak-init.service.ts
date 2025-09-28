import { isPlatformBrowser } from "@angular/common";
import { Inject, Injectable, PLATFORM_ID } from "@angular/core";
import { KeycloakService } from "keycloak-angular";
import { environment } from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class KeycloakInitService {
    constructor(
        private readonly keycloakService: KeycloakService,
        @Inject(PLATFORM_ID) private readonly platformId: Object
    ) {}

    public async initKeycloak(): Promise<void> { 
        if(isPlatformBrowser(this.platformId)){
            await this.keycloakService.init({
                config: {
                    url: environment.keycloak.config.url,
                    realm: environment.keycloak.config.realm,
                    clientId: environment.keycloak.config.clientId
                }, 
                initOptions: {
                    onLoad: 'check-sso', 
                 },
                enableBearerInterceptor: true
            })
        }
    }
}