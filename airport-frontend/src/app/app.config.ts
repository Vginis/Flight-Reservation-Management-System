import { APP_INITIALIZER, ApplicationConfig, importProvidersFrom, PLATFORM_ID } from '@angular/core';
import { provideRouter } from '@angular/router';
import { KeycloakAngularModule } from 'keycloak-angular';
import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { KeycloakInitService } from '../services/keycloak/keycloak-init.service';
import { HttpClientModule } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes), 
    provideClientHydration(), 
    provideAnimations(),
    importProvidersFrom(KeycloakAngularModule, HttpClientModule), 
    {
      provide: APP_INITIALIZER,
      useFactory: initializeApp,
      deps: [KeycloakInitService, PLATFORM_ID],
      multi: true
    }  
  ]
};


function initializeApp(keycloakInitService: KeycloakInitService) {
  return () => keycloakInitService.initKeycloak();
}