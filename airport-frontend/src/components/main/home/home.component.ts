import { Component } from '@angular/core';
import { FlightsearchComponent } from '../search/flightsearch/flightsearch.component';

@Component({
    selector: 'app-home',
    imports: [
        FlightsearchComponent
    ],
    templateUrl: './home.component.html',
    styleUrl: './home.component.css'
})
export class HomeComponent {

}
