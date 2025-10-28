import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { SearchParams } from '../../../../../models/common.models';
import { MatDialog } from '@angular/material/dialog';
import { FlightRepresentation } from '../../../../../models/flight.models';
import { FlightService } from '../../../../../services/backend/flight.service';
import { FlightsFilteringComponent } from '../flights-filtering/flights-filtering.component';

@Component({
  selector: 'app-flights-table',
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatButtonModule,
    FlightsFilteringComponent
  ],
  templateUrl: './flights-table.component.html',
  styleUrl: './flights-table.component.css'
})
export class FlightsTableComponent implements OnInit,AfterViewInit {
  displayedColumns: string[] = ['flightNumber', 'flightUUID', 'departureAirport', 'departureTime', 'arrivalAirport', 'arrivalTime', 'actions'];
  dataSource = new MatTableDataSource<FlightRepresentation>([]);
  sortBy: string = 'flightNumber';
  sortDirection: string = 'asc';
  
  params!: SearchParams;
  pagingOptions = [10,20,50];
  
  
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  
  constructor(
    private readonly flightService: FlightService,
    private readonly dialog: MatDialog
  ){
    this.resetSearchParams();
  }

  ngOnInit(): void {
    this.loadFlightsTable();
  }

  async loadFlightsTable(): Promise<void>{
    const page = this.paginator?.pageIndex || 0;
    const size = this.paginator?.pageSize || 10;

    this.params.index = page;
    this.params.size = size;
    this.flightService.searchFlightsTable(this.params).subscribe({
      next: (data) => {
        this.dataSource.data = data.results;
        this.paginator.length = data.total;
      },
      error: (err) => {
        console.error('Failed to fetch flights:', err);
      }
    })
  }

  resetSearchParams(): void{
    this.params = {
      searchField: '',
      searchValue: '',
      size: 10,
      index: 0,
      sortBy: 'flightNumber',
      sortDirection: 'asc'
    }
    this.loadFlightsTable();
  }

  ngAfterViewInit(): void {
    this.paginator.page.subscribe(() => {
      this.loadFlightsTable()}
    );
    this.sort.sortChange.subscribe(() => {
      this.params.sortBy = this.sort.active;
      this.params.sortDirection = this.sort.direction;
      this.loadFlightsTable();
    });
  }

  async openCreateModal() {
    // const dialogRef = this.dialog.open(UsersCreateModalComponent);
    // await this.reloadTableAfterModalAction(dialogRef);
  }
  
  async editFlight(user: any) {
    // const dialogRef = this.dialog.open(UsersEditModalComponent, {
    //   data: user
    // });

    //await this.reloadTableAfterModalAction(dialogRef);
  }
  
  async deleteFlight(user: any) {
    // const dialogRef = this.dialog.open(UsersDeleteModalComponent, {
    //   data: user
    // });

    //await this.reloadTableAfterModalAction(dialogRef);
  }

  async reloadTableAfterModalAction(dialogRef: any){
    dialogRef.afterClosed().subscribe((result:any) => {
      if(result==='success'){
        this.loadFlightsTable();
      }
    })
  }
  
}
