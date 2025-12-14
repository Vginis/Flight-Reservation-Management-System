import { Component, Input } from '@angular/core';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';

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

  constructor() {
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
    const departure = new Date(this.flight.departureTime);
    const arrival = new Date(this.flight.arrivalTime);

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
}
