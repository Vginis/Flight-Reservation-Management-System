import { Component } from '@angular/core';
import { UsersTableComponent } from './users-table/users-table.component';

@Component({
  selector: 'app-administration',
  imports: [
    UsersTableComponent
  ],
  templateUrl: './administration.component.html',
  styleUrl: './administration.component.css'
})
export class AdministrationComponent {

}
