import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideKeycloak } from 'keycloak-angular';
import { environment } from '../environment';

import { routes } from './app.routes';
import { KeycloakOnLoad } from 'keycloak-js';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes), 
    provideClientHydration(), 
    provideAnimations(), 
    provideKeycloak({
      config: {
        url: environment.keycloak.config.url,
        realm: environment.keycloak.config.realm,
        clientId: environment.keycloak.config.clientId
      },
      initOptions: {
        onLoad: environment.keycloak.initOptions.onLoad as KeycloakOnLoad,
        checkLoginIframe: environment.keycloak.initOptions.checkLoginIframe
      }
    })]
};
