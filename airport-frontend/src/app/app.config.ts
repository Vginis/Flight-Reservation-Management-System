import { APP_INITIALIZER, ApplicationConfig, importProvidersFrom, PLATFORM_ID } from '@angular/core';
import { provideRouter } from '@angular/router';
import { KeycloakAngularModule } from 'keycloak-angular';
import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { KeycloakInitService } from '../services/keycloak/keycloak-init.service';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { LoadingInterceptor } from '../interceptors/loading.interceptor';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration(),
    provideAnimations(),
    importProvidersFrom(KeycloakAngularModule, MatDatepickerModule, MatNativeDateModule),
    provideHttpClient(withInterceptorsFromDi()), 
    {
      provide: APP_INITIALIZER,
      useFactory: initializeApp,
      deps: [KeycloakInitService, PLATFORM_ID],
      multi: true,
    },
    {
      provide: LoadingInterceptor,
      useClass: LoadingInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoadingInterceptor,
      multi: true,
    },
  ],
};


function initializeApp(keycloakInitService: KeycloakInitService) {
  return () => keycloakInitService.initKeycloak();
}