import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { NavbarComponent } from './navbar.component';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { KeycloakService } from 'keycloak-angular';
import { IdentityService } from '../../../services/keycloak/identity.service';
import { UserService } from '../../../services/backend/user.service';
import { UserProfile } from '../../../models/user.models';
import { CommonUtils } from '../../../utils/common.util';
import { environment } from '../../../environments/environment';

describe('NavbarComponent', () => {
    let component: NavbarComponent;
    let fixture: ComponentFixture<NavbarComponent>;

    let keycloakServiceSpy: jasmine.SpyObj<KeycloakService>;
    let dialogSpy: jasmine.SpyObj<MatDialog>;
    let routerSpy: jasmine.SpyObj<Router>;
    let identityServiceSpy: jasmine.SpyObj<IdentityService>;
    let userServiceSpy: jasmine.SpyObj<UserService>;

    const mockUserProfile: UserProfile = {
        id: 1,
        email: 'test@test.com',
        firstName: 'Test',
        lastName: 'User'
    } as UserProfile;

    beforeEach(async () => {
        keycloakServiceSpy = jasmine.createSpyObj('KeycloakService', [
            'isLoggedIn',
            'login',
            'logout'
        ]);

        dialogSpy = jasmine.createSpyObj('MatDialog', ['open']);
        routerSpy = jasmine.createSpyObj('Router', ['navigate']);
        identityServiceSpy = jasmine.createSpyObj('IdentityService', ['hasRole']);
        userServiceSpy = jasmine.createSpyObj('UserService', ['getUserProfile']);

        await TestBed.configureTestingModule({
            imports: [NavbarComponent],
            providers: [
                { provide: KeycloakService, useValue: keycloakServiceSpy },
                { provide: MatDialog, useValue: dialogSpy },
                { provide: Router, useValue: routerSpy },
                { provide: IdentityService, useValue: identityServiceSpy },
                { provide: UserService, useValue: userServiceSpy }
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(NavbarComponent);
        component = fixture.componentInstance;
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should check login status on init', fakeAsync(() => {
        keycloakServiceSpy.isLoggedIn.and.returnValue(true);
        userServiceSpy.getUserProfile.and.returnValue(of(mockUserProfile));

        fixture.detectChanges();
        tick();

        expect(component.loggedIn).toBeTrue();
        expect(component.userProfile).toEqual(mockUserProfile);
    }));

    it('should navigate to complete registration page if profile not found', fakeAsync(() => {
        keycloakServiceSpy.isLoggedIn.and.returnValue(true);
        userServiceSpy.getUserProfile.and.returnValue(
            throwError(() => ({ status: 404 }))
        );

        fixture.detectChanges();
        tick();

        expect(routerSpy.navigate).toHaveBeenCalledWith(['/complete-registration']);
    }));

    it('should call Keycloak login', async () => {
        await component.toLogin();
        expect(keycloakServiceSpy.login).toHaveBeenCalled();
    });

    it('should call Keycloak logout with correct redirect URL', async () => {
        await component.toLogout();

        const expectedUrl =
            environment.keycloak.config.url +
            '/realms/' +
            environment.keycloak.config.realm +
            '/protocol/openid-connect/auth?client_id=frontend&' +
            'redirect_uri=http://localhost:4200&response_type=code&scope=openid';

        expect(keycloakServiceSpy.logout).toHaveBeenCalledWith(expectedUrl);
    });

    it('should open profile dialog', () => {
        component.userProfile = mockUserProfile;

        dialogSpy.open.and.returnValue({
            afterClosed: () => of('cancel')
        } as any);

        component.viewProfile();

        expect(dialogSpy.open).toHaveBeenCalled();
    });

    it('should open reset password modal', () => {
        component.viewResetPasswordModal();
        expect(dialogSpy.open).toHaveBeenCalled();
    });

    it('should navigate to administration page', () => {
        component.navigateToAdministration();
        expect(routerSpy.navigate).toHaveBeenCalledWith(['/administration']);
    });

    it('should navigate to home page', () => {
        component.navigateToHomePage();
        expect(routerSpy.navigate).toHaveBeenCalledWith(['/']);
    });

    it('should navigate to reservations page', () => {
        component.navigateToReservationsPage();
        expect(routerSpy.navigate).toHaveBeenCalledWith(['/reservations']);
    });

    it('should return true if user is admin', () => {
        component.loggedIn = true;
        identityServiceSpy.hasRole.and.callFake((role: string) =>
            role === CommonUtils.SYSTEM_ADMIN
        );

        expect(component.isAdmin()).toBeTrue();
    });

    it('should return false if user is not logged in', () => {
        component.loggedIn = false;
        expect(component.isAdmin()).toBeFalse();
    });

    it('should return true if registration completed', () => {
        component.userProfile = mockUserProfile;
        expect(component.hasCompletedRegistration()).toBeTrue();
    });

    it('should return false if registration not completed', () => {
        component.userProfile = undefined as any;
        expect(component.hasCompletedRegistration()).toBeFalse();
    });
});
