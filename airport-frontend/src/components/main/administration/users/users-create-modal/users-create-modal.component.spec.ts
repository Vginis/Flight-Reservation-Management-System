import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { UsersCreateModalComponent } from './users-create-modal.component';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { UserService } from '../../../../../services/backend/user.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { AirlineService } from '../../../../../services/backend/airline.service';
import { CityCountryService } from '../../../../../services/backend/citycountry.service';
import { of, throwError } from 'rxjs';
import { CommonUtils } from '../../../../../utils/common.util';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('UsersCreateModalComponent', () => {
  let component: UsersCreateModalComponent;
  let fixture: ComponentFixture<UsersCreateModalComponent>;
  let dialogRef: jasmine.SpyObj<MatDialogRef<UsersCreateModalComponent>>;
  let userService: jasmine.SpyObj<UserService>;
  let snackbar: jasmine.SpyObj<SnackbarService>;
  let airlineService: jasmine.SpyObj<AirlineService>;
  let cityCountryService: jasmine.SpyObj<CityCountryService>;

  beforeEach(async () => {
    dialogRef = jasmine.createSpyObj('MatDialogRef', ['close']);
    userService = jasmine.createSpyObj('UserService', ['createUser', 'createAirlineAdministrator']);
    snackbar = jasmine.createSpyObj('SnackbarService', ['success', 'error']);
    airlineService = jasmine.createSpyObj('AirlineService', ['searchAirlines']);
    cityCountryService = jasmine.createSpyObj('CityCountryService', ['smartSearchCountries', 'smartSearchCities']);

    await TestBed.configureTestingModule({
      imports: [UsersCreateModalComponent, NoopAnimationsModule],
      providers: [
        FormBuilder,
        { provide: MatDialogRef, useValue: dialogRef },
        { provide: UserService, useValue: userService },
        { provide: SnackbarService, useValue: snackbar },
        { provide: AirlineService, useValue: airlineService },
        { provide: CityCountryService, useValue: cityCountryService }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UsersCreateModalComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
    expect(component.usersCreateForm).toBeDefined();
  });

  it('should set validators for passenger role', fakeAsync(() => {
    component.usersCreateForm.get('role')?.setValue(CommonUtils.PASSENGER);
    tick();
    const passportControl = component.usersCreateForm.get('passport');
    expect(passportControl?.validator).toBeTruthy();
    expect(component.isPassenger()).toBeTrue();
  }));

  it('should set validators for airline admin role', fakeAsync(() => {
    component.usersCreateForm.get('role')?.setValue(CommonUtils.AIRLINE_ADMIN);
    tick();
    const airlineCodeControl = component.usersCreateForm.get('airlineCode');
    const airlineNameControl = component.usersCreateForm.get('airlineName');
    expect(airlineCodeControl?.validator).toBeTruthy();
    expect(airlineNameControl?.validator).toBeTruthy();
    expect(component.isAirlineAdmin()).toBeTrue();
  }));

  it('should add and remove addresses', () => {
    expect(component.addresses.length).toBe(0);
    component.addAddress();
    expect(component.addresses.length).toBe(1);
    component.removeAddress(0);
    expect(component.addresses.length).toBe(0);
  });

  it('should select a file and update preview', fakeAsync(() => {
    const file = new File(['hello'], 'test.png', { type: 'image/png' });
    const event = { target: { files: [file] } } as unknown as Event;
    component.onFileSelected(event);
    tick();
    expect(component.selectedFile).toBe(file);
  }));

  it('should construct payload correctly for passenger', () => {
    component.usersCreateForm.patchValue({
      username: 'user1',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      phoneNumber: '1234567890',
      role: CommonUtils.PASSENGER,
      passport: 'P1234567'
    });

    const { requestBody, role } = component['constructPayload']();
    expect(role).toBe(CommonUtils.PASSENGER);
    expect(requestBody.passport).toBe('P1234567');
  });

  it('should call createUser for passenger', fakeAsync(() => {
    component.usersCreateForm.patchValue({
      username: 'user1',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      phoneNumber: '1234567890',
      role: CommonUtils.PASSENGER,
      passport: 'P1234567'
    });

    userService.createUser.and.returnValue(of({}));
    component.createNewUser();
    tick();
    expect(userService.createUser).toHaveBeenCalled();
    expect(snackbar.success).toHaveBeenCalled();
    expect(dialogRef.close).toHaveBeenCalledWith('success');
  }));

  it('should handle error in createUser', fakeAsync(() => {
    component.usersCreateForm.patchValue({
      username: 'user1',
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      phoneNumber: '1234567890',
      role: CommonUtils.PASSENGER,
      passport: 'P1234567'
    });

    userService.createUser.and.returnValue(throwError(() => ({ error: { key: 'ERR001' } })));
    component.createNewUser();
    tick();
    expect(snackbar.error).toHaveBeenCalledWith('User was not created successfully! ERR001');
  }));
});
