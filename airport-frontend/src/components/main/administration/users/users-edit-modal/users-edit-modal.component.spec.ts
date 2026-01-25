import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { UsersEditModalComponent } from './users-edit-modal.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { UserService } from '../../../../../services/backend/user.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { CityCountryService } from '../../../../../services/backend/citycountry.service';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('UsersEditModalComponent', () => {
  let component: UsersEditModalComponent;
  let fixture: ComponentFixture<UsersEditModalComponent>;
  let dialogRef: jasmine.SpyObj<MatDialogRef<UsersEditModalComponent>>;
  let userService: jasmine.SpyObj<UserService>;
  let snackbar: jasmine.SpyObj<SnackbarService>;
  let cityCountryService: jasmine.SpyObj<CityCountryService>;

  const mockUserData = {
    username: 'testuser',
    firstName: 'John',
    lastName: 'Doe',
    email: 'john@example.com',
    phoneNumber: '1234567890',
    role: 'PASSENGER',
    addresses: [
      { addressName: 'Home', city: 'NYC', country: 'USA' }
    ]
  };

  beforeEach(async () => {
    const dialogSpy = jasmine.createSpyObj('MatDialogRef', ['close']);
    const userServiceSpy = jasmine.createSpyObj('UserService', ['updateUserDetails', 'getPassengerPassport']);
    const snackbarSpy = jasmine.createSpyObj('SnackbarService', ['success', 'error']);
    const cityCountrySpy = jasmine.createSpyObj('CityCountryService', ['smartSearchCities', 'smartSearchCountries']);

    await TestBed.configureTestingModule({
      imports: [UsersEditModalComponent, NoopAnimationsModule],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: mockUserData },
        { provide: MatDialogRef, useValue: dialogSpy },
        { provide: UserService, useValue: userServiceSpy },
        { provide: SnackbarService, useValue: snackbarSpy },
        { provide: CityCountryService, useValue: cityCountrySpy },
        FormBuilder
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(UsersEditModalComponent);
    component = fixture.componentInstance;

    dialogRef = TestBed.inject(MatDialogRef) as jasmine.SpyObj<MatDialogRef<UsersEditModalComponent>>;
    userService = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;
    snackbar = TestBed.inject(SnackbarService) as jasmine.SpyObj<SnackbarService>;
    cityCountryService = TestBed.inject(CityCountryService) as jasmine.SpyObj<CityCountryService>;

    userService.getPassengerPassport.and.returnValue(of({ passport: 'P12345' }));

    fixture.detectChanges();
  });

  it('should create the component and patch form', fakeAsync(() => {
    expect(component).toBeTruthy();
    expect(component.userUpdateForm.get('username')?.value).toBe('testuser');
  }));

  it('should update user successfully', fakeAsync(() => {
    userService.updateUserDetails.and.returnValue(of({}));

    component.updateUser();
    tick();

    expect(userService.updateUserDetails).toHaveBeenCalled();
    expect(snackbar.success).toHaveBeenCalledWith('User Details updated successfully!');
    expect(dialogRef.close).toHaveBeenCalledWith('success');
  }));

  it('should handle error when updating user', fakeAsync(() => {
    userService.updateUserDetails.and.returnValue(throwError(() => ({ error: { key: 'ERR_UPDATE' } })));

    component.updateUser();
    tick();

    expect(snackbar.error).toHaveBeenCalledWith('User Details were not updated successfully! ERR_UPDATE');
    expect(dialogRef.close).not.toHaveBeenCalled();
  }));

  it('should add and remove address', () => {
    const initialCount = component.addresses.length;

    component.addAddress();
    expect(component.addresses.length).toBe(initialCount + 1);

    component.removeAddress(0);
    expect(component.addresses.length).toBe(initialCount);
  });

  it('should close the dialog', () => {
    component.close();
    expect(dialogRef.close).toHaveBeenCalled();
  });

  it('should not add more than 2 addresses', () => {
    component.addAddress();
    component.addAddress();
    component.addAddress();
    expect(component.addresses.length).toBeLessThanOrEqual(2);
  });
});
