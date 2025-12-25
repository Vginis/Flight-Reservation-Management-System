import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FlightSeatLayoutService } from '../../../services/backend/flightseatlayout.service';
import { Subscription } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { FlightService } from '../../../services/backend/flight.service';
import { SnackbarService } from '../../../services/frontend/snackbar.service';
import { FlightRepresentation, FlightSeatLayoutRepresentation, FlightSeatRepresentation, FlightSeatUpdate, SeatState } from '../../../models/flight.models';

@Component({
  selector: 'app-booking',
  imports: [
    MatIconModule,
    CommonModule
  ],
  templateUrl: './booking.component.html',
  styleUrl: './booking.component.css'
})
export class BookingComponent implements OnInit, OnDestroy {

  flightUUID!: string;
  sub?: Subscription;
  seatRows: number[] = [];
  leftSeatColumns = Array.from({ length: 3 });
  rightSeatColumns = Array.from({ length: 3 });
  flightInformation!: FlightRepresentation;
  selectedSeats = new Set<string>();

  constructor(
    private readonly route: ActivatedRoute,
    private readonly flightSeatLayoutService: FlightSeatLayoutService,
    private readonly flightService: FlightService,
    private readonly snackbar: SnackbarService
  ) {}

  ngOnInit(): void {
    this.flightUUID = this.route.snapshot.paramMap.get('flightUUID') ?? '';

    if(this.flightUUID!==null){
      this.fetchSeatLayoutMap();
      this.sub = this.flightSeatLayoutService.connect(this.flightUUID)
        .subscribe(seat => {
          console.log(seat);
        });
      
      this.flightSeatLayoutService
        .getConnectionStatus()
        .subscribe(status => {
          console.log('🔌 WebSocket status:', status);
        });
    }
    
  }

  private fetchSeatLayoutMap() {
    this.flightService.getFlightSeatLayoutByUUID(this.flightUUID).subscribe({
      next: (data: FlightSeatLayoutRepresentation) => {
        this.loadSeatLayout(data);
        this.loadSelectedSeats(data.flightSeatRepresentationList);
      },
      error: (err) => {
        console.error(`Flight seat layout map not loaded! ${err?.error?.key}`);
      }
    });
  }

  private loadSeatLayout(data: FlightSeatLayoutRepresentation) {
    const cols = data.columns;
    this.seatRows = Array.from({ length: data.rows});
    const half = Math.floor(cols / 2);
    const left = Math.ceil(cols / 2); 

    this.leftSeatColumns = Array.from({ length: left });
    this.rightSeatColumns = Array.from({ length: half });
    this.flightInformation = data.flightInformation;
  }

  private loadSelectedSeats(flightSeatRepresentationList: FlightSeatRepresentation[]): void {
    for(const seat of flightSeatRepresentationList) {
      if(seat.seatReservationState === 'LOCKED') {
        this.selectedSeats.add(this.getSeatId(seat.rowIndex, seat.columnIndex));
      }
    }
  } 

  displayBookingHeaderInformation(): string {
    if(!this.flightInformation) return '';

    return `Please select your tickets for flight ${this.flightInformation.flightNumber} from ${this.flightInformation.departureAirport.city}`+
      `(${this.flightInformation.departureAirport.u3digitCode}) to ${this.flightInformation.arrivalAirport.city}(${this.flightInformation.arrivalAirport.u3digitCode}).`
  } 

  displayAdditionalFlightInfo(): string {
    if(!this.flightInformation) return '';

    return `Departure: ${this.formatFlightTime(this.flightInformation.departureTime)} • 
            Arrival: ${this.formatFlightTime(this.flightInformation.arrivalTime)} • 
            Duration: ${this.displayTravelDuration()}`;
  }

  formatFlightTime(timeString: string): string {
    const date = new Date(timeString);

    const day = date.getDate();
    const month = date.toLocaleString('en-US', { month: 'short' });

    const hours = date.getHours();
    const minutes = date.getMinutes().toString().padStart(2, '0');

    const hour12 = hours % 12 || 12;
    const ampm = hours >= 12 ? 'PM' : 'AM';

    return `${day} ${month}, ${hour12}:${minutes} ${ampm}`;
  }

  displayTravelDuration() : string {
    if(!this.flightInformation) return '';

    const departure = new Date(this.flightInformation.departureTime);
    const arrival = new Date(this.flightInformation.arrivalTime);
    const diffMs = arrival.getTime() - departure.getTime();
    const diffMinutes = Math.floor(diffMs / (1000 * 60));
    const hours = Math.floor(diffMinutes / 60);
    const minutes = diffMinutes % 60;

    const formattedEndTime = arrival.toLocaleTimeString("en-US", {
      hour: "2-digit",
      minute: "2-digit",
      hour12: true
    });

    return (minutes === 0) ? `${hours}h: ${formattedEndTime}` : `${hours}h ${minutes}m: ${formattedEndTime}` 
  }

  onSeatClick(rowIndex: number, colIndex: number): void {
    const seatId = this.getSeatId(rowIndex, colIndex);
    const seatUpdateRequestPayload = this.constructSeatUpdatePayload(rowIndex, colIndex);
    
    if (this.selectedSeats.has(seatId)) {
      this.selectedSeats.delete(seatId);
    } else {
      this.selectedSeats.add(seatId);
    }
    
    this.flightSeatLayoutService.sendSeatUpdate(seatUpdateRequestPayload)
  }

  constructSeatUpdatePayload(rowIndex: number, colIndex: number): FlightSeatUpdate {
    return {
      flightUUID: this.flightUUID,
      rowIndex: rowIndex,
      columnIndex: colIndex,
      seatReservationState: this.resolveNewSeatState(rowIndex, colIndex)
    }
  }

  resolveNewSeatState(rowIndex: number, colIndex: number): SeatState {
    const seatId = this.getSeatId(rowIndex, colIndex);
    if(this.selectedSeats.has(seatId)) {
      console.log("!!!");
      return 'AVAILABLE';
    }

    return 'LOCKED';
  }

  getSeatId(rowIndex: number, colIndex: number): string {
    return `${rowIndex}-${colIndex}`;
  }

  getRightSideIndex(index: number): number {
    return this.leftSeatColumns.length + index;
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
    this.flightSeatLayoutService.disconnect();
  }
}
