import { Component } from '@angular/core';
import { UsersTableComponent } from './users/users-table/users-table.component';
import { CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { AirportsTableComponent } from './airports/airports-table/airports-table.component';

@Component({
  selector: 'app-administration',
  imports: [
    UsersTableComponent,
    AirportsTableComponent,
    CommonModule,
    MatListModule,
    MatIconModule
  ],
  templateUrl: './administration.component.html',
  styleUrl: './administration.component.css'
})
export class AdministrationComponent {
  selectedSection: string | null = 'users';

  menuOptions = [
    { key:'users', label:'Users', icon: 'person'},
    { key: 'airports', label: 'Airports', icon: 'location_on'}
  ]

  selectSection(option: any): void {
    this.selectedSection = option.key;
  }
}
