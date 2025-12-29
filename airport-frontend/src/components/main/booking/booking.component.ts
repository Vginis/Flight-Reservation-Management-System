import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FlightSeatLayoutService } from '../../../services/backend/flightseatlayout.service';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { FlightService } from '../../../services/backend/flight.service';
import { IdentityService } from '../../../services/keycloak/identity.service';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { MatDividerModule } from "@angular/material/divider";
import { MatButtonModule } from '@angular/material/button';
import { SeatSelectionComponent } from './seat-selection/seat-selection.component';

@Component({
  selector: 'app-booking',
  imports: [
    MatIconModule,
    CommonModule,
    MatStepperModule,
    MatDividerModule,
    MatButtonModule,
    SeatSelectionComponent
],
  templateUrl: './booking.component.html',
  styleUrl: './booking.component.css'
})
export class BookingComponent implements OnInit{

  flightUUID!: string;

  @ViewChild('stepper') stepper!: MatStepper;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly flightSeatLayoutService: FlightSeatLayoutService,
    private readonly flightService: FlightService,
    private readonly identityService: IdentityService
  ) {}

  ngOnInit(): void {
    this.flightUUID = this.route.snapshot.paramMap.get('flightUUID') ?? '';
  }
  
  previous(): void {
    this.stepper.previous();
  }

  next(): void {
    this.stepper.next();
  }

}
