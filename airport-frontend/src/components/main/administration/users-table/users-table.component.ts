import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { UserProfile } from '../../../../models/user.models';
import { UserService } from '../../../../services/backend/user.service';
import { SearchParams } from '../../../../models/common.models';
import { CommonUtils } from '../../../../utils/common.util';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { FormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { UsersCreateModalComponent } from '../users-create-modal/users-create-modal.component';
import { UsersEditModalComponent } from '../users-edit-modal/users-edit-modal.component';
import { UsersDeleteModalComponent } from '../users-delete-modal/users-delete-modal.component';

@Component({
  selector: 'app-users-table',
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatOptionModule,
    MatSelectModule,
    MatIconModule,
    MatButtonModule,
    FormsModule
  ],
  templateUrl: './users-table.component.html',
  styleUrl: './users-table.component.css'
})
export class UsersTableComponent implements OnInit,AfterViewInit{
  displayedColumns: string[] = ['username', 'firstName', 'lastName', 'email', 'role', 'actions'];
  dataSource = new MatTableDataSource<UserProfile>([]);
  sortBy: string = 'username';
  sortDirection: string = 'asc';
  filterBy: string = '';
  filterValue: string = '';
  params!: SearchParams;
  pagingOptions = [5,10,20,50];
  filterOptions = [{key:'username',label:'Username'}, {key:'firstName',label:'First Name'}, 
    {key:'lastName', label:'Last Name'}, {key:'email', label: 'Email'}, {key:'role', label:'Role'}];
  
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private readonly userService: UserService,
    private readonly dialog: MatDialog
  ){
    this.resetSearchParams();
  }

  ngOnInit(): void {
    this.loadUsersTable();
  }

  async loadUsersTable(): Promise<void>{
    const page = this.paginator?.pageIndex || 0;
    const size = this.paginator?.pageSize || 10;

    this.params.index = page;
    this.params.size = size;
    this.userService.searchUsers(this.params).subscribe({
      next: (data) => {
        this.dataSource.data = data.results;
        this.paginator.length = data.total;
      },
      error: (err) => {
        console.error('Failed to fetch users:', err);
      }
    })
  }

  resetSearchParams(): void{
    this.params = {
      searchField: '',
      searchValue: '',
      size: 10,
      index: 0,
      sortBy: 'username',
      sortDirection: 'asc'
    }
    this.loadUsersTable();
  }

  isFilteringEmpty(): boolean{
    return this.filterBy==='' || this.filterValue==='';
  }

  resetFiltering(): void{
    this.filterBy = '';
    this.filterValue = '';
    this.resetSearchParams();
  }

  ngAfterViewInit(): void {
    this.paginator.page.subscribe(() => {
      console.log(this.paginator);
      this.loadUsersTable()}
    );
    this.sort.sortChange.subscribe(() => {
      console.log(this.sort.direction);
      this.params.sortBy = this.sort.active;
      this.params.sortDirection = this.sort.direction;
      this.loadUsersTable();
    });
  }

  applyFilter(): void {
    console.log(this.filterValue);
    if(this.filterBy!=='' && this.filterValue!==''){
      this.params.searchField = this.filterBy;
      this.params.searchValue = this.filterValue;
      this.loadUsersTable();
    }
  }

  async openCreateModal() {
    this.dialog.open(UsersCreateModalComponent);
  }

  async editUser(user: any) {
    console.log('Edit user', user);
    this.dialog.open(UsersEditModalComponent);
  }

  async deleteUser(user: any) {
    console.log('Delete user', user);
    this.dialog.open(UsersDeleteModalComponent);
  }

  mapUserRole(role: string): string{
    return CommonUtils.mapRoleToLabel(role);
  }
}
