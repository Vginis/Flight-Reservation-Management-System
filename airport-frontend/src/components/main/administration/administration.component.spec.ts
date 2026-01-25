import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdministrationComponent } from './administration.component';
import { IdentityService } from '../../../services/keycloak/identity.service';
import { of } from 'rxjs';
import { CommonUtils } from '../../../utils/common.util';

describe('AdministrationComponent', () => {
  let component: AdministrationComponent;
  let fixture: ComponentFixture<AdministrationComponent>;
  let identityService: jasmine.SpyObj<IdentityService>;

  beforeEach(async () => {
    const identitySpy = jasmine.createSpyObj('IdentityService', ['getKeycloakProfileIdentity']);

    await TestBed.configureTestingModule({
      imports: [AdministrationComponent],
      providers: [
        { provide: IdentityService, useValue: identitySpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AdministrationComponent);
    component = fixture.componentInstance;
    identityService = TestBed.inject(IdentityService) as jasmine.SpyObj<IdentityService>;
  });

  it('should create the component', () => {
    identityService.getKeycloakProfileIdentity.and.returnValue({ 
        id: '1',
        username: 'jdoe',
        firstName: 'john',
        lastName: 'doe',
        email: 'email@email.com',
        roles: [] 
    });
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should filter menu options based on SYSTEM_ADMIN role', () => {
    identityService.getKeycloakProfileIdentity.and.returnValue({ 
        id: '1',
        username: 'jdoe',
        firstName: 'john',
        lastName: 'doe',
        email: 'email@email.com',
        roles: [CommonUtils.SYSTEM_ADMIN] 
    });
    fixture.detectChanges();

    expect(component.filteredMenuOptions.length).toBeGreaterThan(0);
    expect(component.filteredMenuOptions.every(opt => opt.rolesAllowed.includes(CommonUtils.SYSTEM_ADMIN))).toBeTrue();
    expect(component.selectedSection).toBe('users');
  });

  it('should filter menu options based on AIRLINE_ADMIN role', () => {
    identityService.getKeycloakProfileIdentity.and.returnValue({ 
        id: '1',
        username: 'jdoe',
        firstName: 'john',
        lastName: 'doe',
        email: 'email@email.com',
        roles: [CommonUtils.SYSTEM_ADMIN] 
    });
    fixture.detectChanges();

    expect(component.filteredMenuOptions.length).toBeGreaterThan(0);
    expect(component.filteredMenuOptions.every(opt => opt.rolesAllowed.includes(CommonUtils.AIRLINE_ADMIN))).toBeTrue();
    expect(component.selectedSection).toBe('aircrafts');
  });

  it('should set selectedSection when selectSection is called', () => {
    identityService.getKeycloakProfileIdentity.and.returnValue({ 
        id: '1',
        username: 'jdoe',
        firstName: 'john',
        lastName: 'doe',
        email: 'email@email.com',
        roles: [CommonUtils.SYSTEM_ADMIN] 
    });
    fixture.detectChanges();

    const option = { key: 'airlines', label: 'Airlines', rolesAllowed: [CommonUtils.SYSTEM_ADMIN] };
    component.selectSection(option);
    expect(component.selectedSection).toBe('airlines');
  });

  it('should handle empty roles gracefully', () => {
    identityService.getKeycloakProfileIdentity.and.returnValue({ 
        id: '1',
        username: 'jdoe',
        firstName: 'john',
        lastName: 'doe',
        email: 'email@email.com',
        roles: [] 
    });
    fixture.detectChanges();

    expect(component.filteredMenuOptions.length).toBe(0);
    expect(component.selectedSection).toBe('');
  });
});
