import { Routes } from '@angular/router';
import { HomeComponent } from '../components/main/home/home.component';
import { AccessGuard } from '../guards/access/access.guard';
import { BookingComponent } from '../components/main/booking/booking.component';
import { AdministrationComponent } from '../components/main/administration/administration.component';
import { SelectFlightComponent } from '../components/main/booking/select-flight/select-flight.component';

export const routes: Routes = [
    { path: '', component: HomeComponent},
    { path: 'booking', component: BookingComponent, canActivate: [AccessGuard] },
    { path: 'administration', component: AdministrationComponent, canActivate: [AccessGuard] },
    { path: 'select-flight', component: SelectFlightComponent, canActivate: [AccessGuard] }
];
