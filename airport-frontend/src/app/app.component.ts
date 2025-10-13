import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HomeComponent } from '../components/main/home/home.component';
import { LoadingService } from '../services/frontend/loading.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../components/common/navbar/navbar.component';

@Component({
    selector: 'app-root',
    imports: [
        RouterOutlet, 
        NavbarComponent,
        MatProgressSpinnerModule,
        CommonModule
    ],
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    loading$ = this.loadingService.loading$;
    constructor(private readonly loadingService: LoadingService){};
}
