import { Component, Input } from '@angular/core';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { CommonUtils } from '../../../../utils/common.util';

@Component({
  selector: 'app-single-flightselect',
  imports: [
    MatDividerModule,
    MatIconModule
  ],
  templateUrl: './single-flightselect.component.html',
  styleUrl: './single-flightselect.component.css'
})
export class SingleFlightselectComponent {
  @Input() flight: any;
  commonUtils = CommonUtils;
  constructor(
    private readonly router: Router,
  ) {
  }

  onFlightClick(): void {
    const flightUUID = this.flight.flightUUID;
    this.router.navigate(['/booking', flightUUID]);
  }

  onFlightKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.onFlightClick();
    }
  }
}
