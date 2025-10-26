import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { SearchParams } from '../../../../../models/common.models';
import { MatDialog } from '@angular/material/dialog';
import { AircraftRepresentation } from '../../../../../models/aircraft.model';
import { AircraftService } from '../../../../../services/backend/aircraft.service';
import { AircraftsCreateModalComponent } from '../aircrafts-create-modal/aircrafts-create-modal.component';
import { AircraftsUpdateModalComponent } from '../aircrafts-update-modal/aircrafts-update-modal.component';
import { AircraftsDeleteModalComponent } from '../aircrafts-delete-modal/aircrafts-delete-modal.component';

@Component({
  selector: 'app-aircrafts-table',
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
  templateUrl: './aircrafts-table.component.html',
  styleUrl: './aircrafts-table.component.css'
})
export class AircraftsTableComponent implements OnInit, AfterViewInit{
  displayedColumns: string[] = ['aircraftName', 'aircraftCapacity','actions'];
  dataSource = new MatTableDataSource<AircraftRepresentation>([]);
  sortBy: string = 'aircraftName';
  sortDirection: string = 'asc';
  filterBy: string = '';
  filterValue: string = '';
  params!: SearchParams;
  pagingOptions = [10,20,50];
  filterOptions = [{key:'aircraftName',label:'Aircraft Name'}, {key:'aircraftCapacity',label:'Capacity'}];
  
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private readonly aircraftService: AircraftService,
    private readonly dialog: MatDialog
  ){
    this.resetSearchParams();
  }

  ngOnInit(): void {
    this.loadAircraftsTable();
  }

  resetSearchParams(): void{
    this.params = {
      searchField: '',
      searchValue: '',
      size: 10,
      index: 0,
      sortBy: 'aircraftName',
      sortDirection: 'asc'
    }
    this.loadAircraftsTable();
  }

  async loadAircraftsTable(): Promise<void>{
    const page = this.paginator?.pageIndex || 0;
    const size = this.paginator?.pageSize || 10;

    this.params.index = page;
    this.params.size = size;
    this.aircraftService.searchAircrafts(this.params).subscribe({
      next: (data) => {
        this.dataSource.data = data.results;
        this.paginator.length = data.total;
      },
      error: (err) => {
        console.error('Failed to fetch airports:', err);
      }
    })
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
      this.loadAircraftsTable()}
    );
    this.sort.sortChange.subscribe(() => {
      this.params.sortBy = this.sort.active;
      this.params.sortDirection = this.sort.direction;
      this.loadAircraftsTable();
    });
  }

  applyFilter(): void {
    if(this.filterBy!=='' && this.filterValue!==''){
      this.params.searchField = this.filterBy;
      this.params.searchValue = this.filterValue;
      this.loadAircraftsTable();
    }
  }

  async openCreateModal() {
    const dialogRef = this.dialog.open(AircraftsCreateModalComponent);
    await this.reloadTableAfterModalAction(dialogRef);
  }
    
  async editAircraft(aircraft: AircraftRepresentation) {
    const dialogRef = this.dialog.open(AircraftsCreateModalComponent, {
      data: aircraft
    });

    await this.reloadTableAfterModalAction(dialogRef);
  }
  
  async deleteAircraft(aircraft: any) {
    const dialogRef = this.dialog.open(AircraftsDeleteModalComponent, {
      data: aircraft
    });

    await this.reloadTableAfterModalAction(dialogRef);
  }

  async reloadTableAfterModalAction(dialogRef: any){
    dialogRef.afterClosed().subscribe((result:any) => {
      if(result==='success'){
        this.loadAircraftsTable();
      }
    })
  }
}
