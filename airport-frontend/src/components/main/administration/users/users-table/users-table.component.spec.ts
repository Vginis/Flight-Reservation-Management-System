import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { UsersTableComponent } from './users-table.component';
import { UserService } from '../../../../../services/backend/user.service';
import { MatDialog } from '@angular/material/dialog';
import { of } from 'rxjs';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('UsersTableComponent', () => {
  let component: UsersTableComponent;
  let fixture: ComponentFixture<UsersTableComponent>;
  let userService: jasmine.SpyObj<UserService>;
  let matDialog: jasmine.SpyObj<MatDialog>;

  const mockUsers = {
    total: 2,
    results: [
      { username: 'user1', firstName: 'John', lastName: 'Doe', email: 'john@example.com', role: 'PASSENGER' },
      { username: 'user2', firstName: 'Jane', lastName: 'Smith', email: 'jane@example.com', role: 'ADMIN' }
    ]
  };

  beforeEach(async () => {
    userService = jasmine.createSpyObj('UserService', ['searchUsers']);
    matDialog = jasmine.createSpyObj('MatDialog', ['open']);

    userService.searchUsers.and.returnValue(of(mockUsers));
    await TestBed.configureTestingModule({
      imports: [UsersTableComponent, NoopAnimationsModule],
      providers: [
        { provide: UserService, useValue: userService },
        { provide: MatDialog, useValue: matDialog }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(UsersTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load users table on init', fakeAsync(() => {
    component.loadUsersTable();
    tick();
    expect(userService.searchUsers).toHaveBeenCalled();
    expect(component.dataSource.data.length).toBe(2);
    expect(component.paginator.length).toBe(2);
  }));

  it('should check if filtering is empty', () => {
    component.filterBy = '';
    component.filterValue = '';
    expect(component.isFilteringEmpty()).toBeTrue();

    component.filterBy = 'username';
    component.filterValue = '';
    expect(component.isFilteringEmpty()).toBeTrue();

    component.filterBy = 'username';
    component.filterValue = 'test';
    expect(component.isFilteringEmpty()).toBeFalse();
  });

  it('should reset filtering', fakeAsync(() => {
    component.filterBy = 'username';
    component.filterValue = 'test';
    component.resetFiltering();
    tick();

    expect(component.filterBy).toBe('');
    expect(component.filterValue).toBe('');
    expect(userService.searchUsers).toHaveBeenCalled();
  }));

  it('should apply filter only when values exist', fakeAsync(() => {
    component.filterBy = 'username';
    component.filterValue = 'user1';
    component.applyFilter();
    tick();
    expect(component.params.searchField).toBe('username');
    expect(component.params.searchValue).toBe('user1');
    expect(userService.searchUsers).toHaveBeenCalled();
  }));

  it('should open create modal and reload table after success', fakeAsync(() => {
    const afterClosedSpy = jasmine.createSpyObj('afterClosed', ['subscribe']);
    matDialog.open.and.returnValue({ afterClosed: () => of('success') } as any);

    component.openCreateModal();
    tick();
    expect(matDialog.open).toHaveBeenCalled();
    expect(userService.searchUsers).toHaveBeenCalled();
  }));

  it('should open edit modal and reload table after success', fakeAsync(() => {
    matDialog.open.and.returnValue({ afterClosed: () => of('success') } as any);

    component.editUser(mockUsers.results[0]);
    tick();
    expect(matDialog.open).toHaveBeenCalledWith(jasmine.any(Function), { data: mockUsers.results[0] });
    expect(userService.searchUsers).toHaveBeenCalled();
  }));

  it('should open delete modal and reload table after success', fakeAsync(() => {
    matDialog.open.and.returnValue({ afterClosed: () => of('success') } as any);

    component.deleteUser(mockUsers.results[0]);
    tick();
    expect(matDialog.open).toHaveBeenCalledWith(jasmine.any(Function), { data: mockUsers.results[0] });
    expect(userService.searchUsers).toHaveBeenCalled();
  }));

});
