import { Routes } from '@angular/router';
import { HomeComponent } from '../components/main/home/home.component';
import { AccessGuard } from '../guards/access/access.guard';
import { BookingComponent } from '../components/main/booking/booking.component';
import { AdministrationComponent } from '../components/main/administration/administration.component';
import { SelectFlightComponent } from '../components/main/booking/select-flight/select-flight.component';
import { CompleteRegistrationComponent } from '../components/main/complete-registration/complete-registration.component';
import { ReservationsComponent } from '../components/main/reservations/reservations.component';

export const routes: Routes = [
    { path: '', component: HomeComponent},
    { path: 'booking/:flightUUID', component: BookingComponent, canActivate: [AccessGuard] },
    { path: 'administration', component: AdministrationComponent, canActivate: [AccessGuard] },
    { path: 'select-flight', component: SelectFlightComponent, canActivate: [AccessGuard] },
    { path: 'complete-registration', component: CompleteRegistrationComponent, canActivate: [AccessGuard] },
    { path: 'reservations', component: ReservationsComponent, canActivate: [AccessGuard] }
];
