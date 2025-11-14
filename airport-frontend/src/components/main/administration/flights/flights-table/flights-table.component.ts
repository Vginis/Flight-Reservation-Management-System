import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { FlightSearchParams } from '../../../../../models/common.models';
import { MatDialog } from '@angular/material/dialog';
import { FlightRepresentation } from '../../../../../models/flight.models';
import { FlightService } from '../../../../../services/backend/flight.service';
import { FlightsFilteringComponent } from '../flights-filtering/flights-filtering.component';
import { CommonUtils } from '../../../../../utils/common.util';
import { FlightsCreateModalComponent } from '../flights-create-modal/flights-create-modal.component';
import { MatMenuModule } from '@angular/material/menu';
import { FlightsCancelModalComponent } from '../flights-cancel-modal/flights-cancel-modal.component';
import { FlightStatusUpdateModalComponent } from '../flight-statusupdate-modal/flight-statusupdate-modal.component';

@Component({
  selector: 'app-flights-table',
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    FlightsFilteringComponent
  ],
  templateUrl: './flights-table.component.html',
  styleUrl: './flights-table.component.css'
})
export class FlightsTableComponent implements OnInit,AfterViewInit {
  displayedColumns: string[] = ['flightNumber', 'flightUUID', 'flightStatus', 'departureAirport', 'departureTime', 'arrivalAirport', 'arrivalTime', 'actions'];
  dataSource = new MatTableDataSource<FlightRepresentation>([]);
  sortBy: string = 'flightNumber';
  sortDirection: string = 'asc';
  
  params!: FlightSearchParams;
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
      departureDate: '',
      arrivalDate: '',
      departureAirport: null,
      arrivalAirport: null,
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
    const dialogRef = this.dialog.open(FlightsCreateModalComponent);
    await this.reloadTableAfterModalAction(dialogRef);
  }
  
  async editFlight(flight: any) {
    const dialogRef = this.dialog.open(FlightsCreateModalComponent, {
      data: flight
    });

    await this.reloadTableAfterModalAction(dialogRef);
  }
  
  async cancelFlight(flight: any) {
    const dialogRef = this.dialog.open(FlightsCancelModalComponent, {
      data: flight
    });

    await this.reloadTableAfterModalAction(dialogRef);
  }

  async updateFlightStatus(flight: any) {
    const dialogRef = this.dialog.open(FlightStatusUpdateModalComponent, {
      data: flight
    });

    await this.reloadTableAfterModalAction(dialogRef); 
  }

  async reloadTableAfterModalAction(dialogRef: any){
    dialogRef.afterClosed().subscribe((result:any) => {
      if(result==='success'){
        this.loadFlightsTable();
      }
    })
  }

  handleFormSubmit(formData: any): void {
    this.params.searchField = formData.filterBy;
    this.params.searchValue = formData.filterValue;
    this.params.departureAirport = formData.departureAirport?.airportId;
    this.params.arrivalAirport = formData.arrivalAirport?.airportId;
    this.params.departureDate = (formData.departureDate==='') ? '' :
      CommonUtils.formatDateForDateTimePattern(formData.departureDate);
    this.params.arrivalDate = (formData.arrivalDate==='') ? '' : 
      CommonUtils.formatDateForDateTimePattern(formData.arrivalDate);
    
    this.loadFlightsTable();
  }

  flightIsNotInFinalStatus(flight: FlightRepresentation): boolean {
    return flight.flightStatus !== 'CANCELLED' && flight.flightStatus !== 'ARRIVED';
  }
}
