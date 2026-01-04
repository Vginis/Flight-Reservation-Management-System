import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { ReservationsSearchParams } from '../../../models/common.models';
import { MatDialog } from '@angular/material/dialog';
import { ReservationService } from '../../../services/backend/reservation.service';
import { MatButtonModule } from '@angular/material/button';
import { ViewReservationDetailsModalComponent } from './view-reservation-details-modal/view-reservation-details-modal.component';

@Component({
  selector: 'app-reservations',
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './reservations.component.html',
  styleUrl: './reservations.component.css'
})
export class ReservationsComponent implements OnInit, AfterViewInit{

  displayedColumns: string[] = ['reservationUUID', 'travelFrom', 'createdAt', 'viewDetails'];
  dataSource = new MatTableDataSource<any>([]);
  sortDirection: string = 'asc';
  filterBy: string = '';
  filterValue: string = '';
  params!: ReservationsSearchParams;
  pagingOptions = [10,20,50];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
      private readonly reservationService: ReservationService,
      private readonly dialog: MatDialog
    ){
    this.resetSearchParams();
  }

  ngOnInit(): void {
    this.loadReservationsTable();
  }

  resetSearchParams(): void {
    this.params = {
      size: 10,
      index: 0,
      sortDirection: 'asc'
    }
    this.loadReservationsTable();
  }

  async loadReservationsTable(): Promise<void>{
    const page = this.paginator?.pageIndex || 0;
    const size = this.paginator?.pageSize || 10;

    this.params.index = page;
    this.params.size = size;
    this.reservationService.searchReservations(this.params).subscribe({
      next: (data) => {
        this.dataSource.data = data.results;
        this.paginator.length = data.total;
      },
      error: (err) => {
        console.error('Failed to fetch user reservations:', err);
      }
    })
  }

  displayDate(createdAt: string): string {
    const date = createdAt.split("T")[0];
    return date;
  }

  displayDepartureAirport(reservation: any): string {
    const airport = reservation?.ticketList[0]?.flightRepresentation.departureAirport;
    return `${airport.city}(${airport.u3digitCode})`
  }

  displayArrivalAirport(reservation: any): string {
    const airport = reservation?.ticketList[0]?.flightRepresentation.arrivalAirport;
    return `${airport.city}(${airport.u3digitCode})`
  }

  async openViewDetailsModal(reservation: any) {
    this.dialog.open(ViewReservationDetailsModalComponent, {
      data: reservation
    });
  }

  ngAfterViewInit(): void {
    this.paginator.page.subscribe(() => {
      this.loadReservationsTable()}
    );
    this.sort.sortChange.subscribe(() => {
      this.params.sortDirection = this.sort.direction;
      this.loadReservationsTable();
    });
  }
}
