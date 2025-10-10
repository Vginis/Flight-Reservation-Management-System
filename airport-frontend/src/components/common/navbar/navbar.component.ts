import { Component, OnInit } from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { KeycloakService } from 'keycloak-angular';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environments/environment';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { ViewProfileModalComponent } from '../viewprofile/view.profile.modal.component';

@Component({
    selector: 'app-navbar',
    imports: [
        MatMenuModule,
        MatToolbarModule, 
        MatIconModule,
        CommonModule,
        MatSelectModule,
        MatButtonModule
    ],
    templateUrl: './navbar.component.html',
    styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit{
  loggedIn = false;

  constructor(
    private readonly keycloakService: KeycloakService,
    private dialog: MatDialog
  ){}

  ngOnInit(): void {
    this.checkLoginStatus();
  }

  private async checkLoginStatus() {
    this.loggedIn = await this.keycloakService.isLoggedIn();
  }

  async toLogin() {
    await this.keycloakService.login();
  }

  async toLogout() {
    const loginUrl = environment.keycloak.config.url 
      + '/realms/' + environment.keycloak.config.realm 
      +'/protocol/openid-connect/auth?client_id=frontend&' + 
      'redirect_uri=http://localhost:4200&response_type=code&scope=openid';
    await this.keycloakService.logout(loginUrl);
  }

  async viewProfile() {
    this.dialog.open(ViewProfileModalComponent);
  }

  async changePassword() {
    console.log("Change Password triggered");
  }

}
