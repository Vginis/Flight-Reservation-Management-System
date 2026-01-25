import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { UsersDeleteModalComponent } from './users-delete-modal.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { UserService } from '../../../../../services/backend/user.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { of, throwError } from 'rxjs';

describe('UsersDeleteModalComponent', () => {
  let component: UsersDeleteModalComponent;
  let fixture: ComponentFixture<UsersDeleteModalComponent>;
  let dialogRef: jasmine.SpyObj<MatDialogRef<UsersDeleteModalComponent>>;
  let userService: jasmine.SpyObj<UserService>;
  let snackbar: jasmine.SpyObj<SnackbarService>;

  const mockUserData = { 
    id: 1, 
    username: 'testuser',
    firstName: 'John',
    lastName: 'Doe',
    email: 'email@email.com',
    role: 'admin',
    phoneNumber: '1231231231',
    addresses: []
 };

  beforeEach(async () => {
    const dialogSpy = jasmine.createSpyObj('MatDialogRef', ['close']);
    const userServiceSpy = jasmine.createSpyObj('UserService', ['deleteUser']);
    const snackbarSpy = jasmine.createSpyObj('SnackbarService', ['success', 'error']);

    await TestBed.configureTestingModule({
      imports: [UsersDeleteModalComponent],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: mockUserData },
        { provide: MatDialogRef, useValue: dialogSpy },
        { provide: UserService, useValue: userServiceSpy },
        { provide: SnackbarService, useValue: snackbarSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(UsersDeleteModalComponent);
    component = fixture.componentInstance;

    dialogRef = TestBed.inject(MatDialogRef) as jasmine.SpyObj<MatDialogRef<UsersDeleteModalComponent>>;
    userService = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;
    snackbar = TestBed.inject(SnackbarService) as jasmine.SpyObj<SnackbarService>;

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
    expect(component.userProfileData).toEqual(mockUserData);
  });

  it('should delete user successfully', fakeAsync(() => {
    userService.deleteUser.and.returnValue(of({}));
    
    component.deleteUser();
    tick();

    expect(userService.deleteUser).toHaveBeenCalledWith(mockUserData.id);
    expect(snackbar.success).toHaveBeenCalledWith('User deleted successfully');
    expect(dialogRef.close).toHaveBeenCalledWith('success');
  }));

  it('should handle error when deleting user', fakeAsync(() => {
    userService.deleteUser.and.returnValue(throwError(() => ({ error: { key: 'ERR_DELETE' } })));
    
    component.deleteUser();
    tick();

    expect(snackbar.error).toHaveBeenCalledWith('User was not deleted:ERR_DELETE');
    expect(dialogRef.close).not.toHaveBeenCalled();
  }));

  it('should close the dialog without deleting', () => {
    component.close();
    expect(dialogRef.close).toHaveBeenCalled();
  });
});
