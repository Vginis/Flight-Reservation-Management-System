import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { AirportRepresentation } from '../../../../../models/airport.models';
import { SearchParams } from '../../../../../models/common.models';
import { AirportService } from '../../../../../services/backend/airport.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatOptionModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { AirportsCreateModalComponent } from '../airports-create-modal/airports-create-modal.component';
import { AirportsEditModalComponent } from '../airports-edit-modal/airports-edit-modal.component';
import { AirportsDeleteModalComponent } from '../airports-delete-modal/airports-delete-modal.component';

@Component({
  selector: 'app-airports-table',
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
  templateUrl: './airports-table.component.html',
  styleUrl: './airports-table.component.css'
})
export class AirportsTableComponent implements OnInit, AfterViewInit{
  displayedColumns: string[] = ['airportName', 'city', 'country', 'u3digitCode', 'actions'];
  dataSource = new MatTableDataSource<AirportRepresentation>([]);
  sortBy: string = 'airportName';
  sortDirection: string = 'asc';
  filterBy: string = '';
  filterValue: string = '';
  params!: SearchParams;
  pagingOptions = [10,20,50];
  filterOptions = [{key:'airportName',label:'Airport Name'}, {key:'city',label:'City'}, 
    {key:'country', label:'Country'}, {key:'u3digitCode', label: 'Airport Code'}];

  constructor(
    private readonly dialog: MatDialog,
    private readonly airportService: AirportService
  ) {
    this.resetSearchParams();
  }

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit(): void {
    this.loadAirportsTable();
  }

  resetSearchParams(): void{
    this.params = {
      searchField: '',
      searchValue: '',
      size: 10,
      index: 0,
      sortBy: 'airportName',
      sortDirection: 'asc'
    }
    this.loadAirportsTable();
  }

  async loadAirportsTable(): Promise<void>{
    const page = this.paginator?.pageIndex || 0;
    const size = this.paginator?.pageSize || 10;

    this.params.index = page;
    this.params.size = size;
    this.airportService.searchAirports(this.params).subscribe({
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
      this.loadAirportsTable()}
    );
    this.sort.sortChange.subscribe(() => {
      this.params.sortBy = this.sort.active;
      this.params.sortDirection = this.sort.direction;
      this.loadAirportsTable();
    });
  }

  applyFilter(): void {
    if(this.filterBy!=='' && this.filterValue!==''){
      this.params.searchField = this.filterBy;
      this.params.searchValue = this.filterValue;
      this.loadAirportsTable();
    }
  }

  async openCreateModal() {
      const dialogRef = this.dialog.open(AirportsCreateModalComponent);
      await this.reloadTableAfterModalAction(dialogRef);
  }
  
  async editAirport(airport: any) {
    const dialogRef = this.dialog.open(AirportsEditModalComponent, {
      data: airport
    });

    await this.reloadTableAfterModalAction(dialogRef);
  }
  
  async deleteAirport(airport: any) {
    const dialogRef = this.dialog.open(AirportsDeleteModalComponent, {
      data: airport
    });

    await this.reloadTableAfterModalAction(dialogRef);
  }

  async reloadTableAfterModalAction(dialogRef: any){
    dialogRef.afterClosed().subscribe((result:any) => {
      if(result==='success'){
        this.loadAirportsTable();
      }
    })
  }
}
