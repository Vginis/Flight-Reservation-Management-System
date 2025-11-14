import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { SingleFlightselectComponent } from '../single-flightselect/single-flightselect.component';
import { AirlineService } from '../../../../services/backend/airline.service';
import { LoadingService } from '../../../../services/frontend/loading.service';
import { SnackbarService } from '../../../../services/frontend/snackbar.service';

@Component({
  selector: 'app-select-flight',
  imports: [
    SingleFlightselectComponent,
    CommonModule,
    MatIconModule
  ],
  templateUrl: './select-flight.component.html',
  styleUrl: './select-flight.component.css'
})
export class SelectFlightComponent {
  flights: any;
  airlineCodes = new Set<string>();

  constructor(
    private readonly router: Router,
    private readonly airlineService: AirlineService,
    private readonly loadingService: LoadingService,
    private readonly snackbarService: SnackbarService
  ) {
    const nav = this.router.getCurrentNavigation();
    this.flights = nav?.extras?.state?.['flights'].results;
    this.airlineCodes = new Set(this.flights.map((f: any) => f.airlineU2DigitCode));
    this.loadLogosInFlights();
  }

  loadLogosInFlights(): void {
    this.loadingService.show();
    this.airlineService.getAirlineLogos(this.airlineCodes).subscribe({
      next: (data) => {
        
        for(const flight of this.flights){
          flight.content = data.find((l:any) => l.airlineCode === flight.airlineU2DigitCode).content;
        }
        console.log(this.flights);
        this.loadingService.hide();
      },
      error: (error) => {
        this.snackbarService.error(`An error has occured while fetching Airline logo images: ${error}`);
        this.loadingService.hide();
      }
    })
  }
}
