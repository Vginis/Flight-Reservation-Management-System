import { Component, Input, OnDestroy, OnInit, Output, EventEmitter } from '@angular/core';
import { FlightRepresentation, FlightSeatLayoutRepresentation, FlightSeatRepresentation, FlightSeatUpdate, SeatState } from '../../../../models/flight.models';
import { Subscription } from 'rxjs';
import { FlightSeatLayoutService } from '../../../../services/backend/flightseatlayout.service';
import { FlightService } from '../../../../services/backend/flight.service';
import { IdentityService } from '../../../../services/keycloak/identity.service';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-seat-selection',
  imports: [
    MatIconModule,
    CommonModule
  ],
  templateUrl: './seat-selection.component.html',
  styleUrl: './seat-selection.component.css'
})
export class SeatSelectionComponent implements OnInit, OnDestroy{

  @Input() flightUUID!: string;
  @Input() selectedSeats = new Set<string>();
  @Output() selectedSeatsChange = new EventEmitter<Set<string>>();

  flightInformation!: FlightRepresentation;
  sub?: Subscription;
  seatRows: number[] = [];
  leftSeatColumns = Array.from({ length: 3 });
  rightSeatColumns = Array.from({ length: 3 });
  bookedSeats = new Set<string>();

  constructor(
    private readonly flightSeatLayoutService: FlightSeatLayoutService,
    private readonly flightService: FlightService,
    private readonly identityService: IdentityService
  ) {}

  ngOnInit(): void {
    if(this.flightUUID!==null){
      this.fetchSeatLayoutMap();
      this.sub = this.flightSeatLayoutService.connect(this.flightUUID)
        .subscribe();
      
      this.flightSeatLayoutService
        .getConnectionStatus()
        .subscribe();

      this.flightSeatLayoutService.selectedSeats$
        .subscribe(seats => this.selectedSeats = seats);
  
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
      const seatId = this.getSeatId(seat.rowIndex, seat.columnIndex);
      const loggedInUsername = this.identityService.getKeycloakProfileIdentity()?.username;
      
      if(seat.seatReservationState === 'LOCKED' && loggedInUsername === seat.lastUpdatedBy) {
        this.selectedSeats.add(seatId);
      }

      if(seat.seatReservationState === 'BOOKED' || (seat.seatReservationState === 'LOCKED' && loggedInUsername !== seat.lastUpdatedBy)) {
        this.bookedSeats.add(seatId);
      }
    }
    const newSet = new Set(this.selectedSeats);
    this.selectedSeatsChange.emit(newSet);;
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
    const payload = this.constructSeatUpdatePayload(rowIndex, colIndex);

    if (this.selectedSeats.has(seatId)) {
      this.flightSeatLayoutService.deselectSeat(seatId, {
        ...payload,
        seatReservationState: 'AVAILABLE'
      });
    } else {
      this.flightSeatLayoutService.selectSeat(seatId, {
        ...payload,
        seatReservationState: 'LOCKED'
      });
    }
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
    return this.selectedSeats.has(seatId) ? 'AVAILABLE' : 'LOCKED';
  }

  isBooked(rowIndex: number, colIndex: number): boolean {
    return this.bookedSeats.has(this.getSeatId(rowIndex, colIndex));
  }

  isLocked(rowIndex: number, colIndex: number): boolean {
    return this.selectedSeats.has(this.getSeatId(rowIndex, colIndex));
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
