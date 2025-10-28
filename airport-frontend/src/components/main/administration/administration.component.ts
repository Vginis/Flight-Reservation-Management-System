import { Component, OnInit } from '@angular/core';
import { UsersTableComponent } from './users/users-table/users-table.component';
import { CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { AirportsTableComponent } from './airports/airports-table/airports-table.component';
import { AirlinesTableComponent } from './airlines/airlines-table/airlines-table.component';
import { CommonUtils } from '../../../utils/common.util';
import { IdentityService } from '../../../services/keycloak/identity.service';
import { AircraftsTableComponent } from './aircrafts/aircrafts-table/aircrafts-table.component';
import { FlightsTableComponent } from './flights/flights-table/flights-table.component';

@Component({
  selector: 'app-administration',
  imports: [
    UsersTableComponent,
    AirportsTableComponent,
    AirlinesTableComponent,
    AircraftsTableComponent,
    FlightsTableComponent,
    CommonModule,
    MatListModule,
    MatIconModule
  ],
  templateUrl: './administration.component.html',
  styleUrl: './administration.component.css'
})
export class AdministrationComponent implements OnInit{
  selectedSection: string | null = '';
  menuOptions = [
    { key:'users', label:'Users', icon: 'person', rolesAllowed: [CommonUtils.SYSTEM_ADMIN]},
    { key: 'airports', label: 'Airports', icon: 'location_on', rolesAllowed: [CommonUtils.SYSTEM_ADMIN]},
    { key: 'airlines', label: 'Airlines', icon: 'local_airport', rolesAllowed: [CommonUtils.SYSTEM_ADMIN]},
    { key: 'aircrafts', label: 'Aircrafts', icon: 'flight_takeoff', rolesAllowed: [CommonUtils.AIRLINE_ADMIN]},
    { key: 'flights', label: 'Flights', icon: 'card_travel', rolesAllowed: [CommonUtils.AIRLINE_ADMIN]},  
  ]
  filteredMenuOptions: any[] = [];
  userRoles: string[] = [];

  constructor(private readonly identityService: IdentityService) { }

  ngOnInit() {
    this.userRoles = this.identityService.getKeycloakProfileIdentity()?.roles ?? [];

    this.filteredMenuOptions = this.menuOptions.filter((option:any) => 
      option?.rolesAllowed.some((role:any) => this.userRoles.includes(role))
    );

    this.selectedSection = (this.userRoles.includes(CommonUtils.SYSTEM_ADMIN)) ? 'users' : 'aircrafts';
  }

  selectSection(option: any): void {
    this.selectedSection = option.key;
  }
}
