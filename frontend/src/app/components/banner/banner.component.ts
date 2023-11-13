import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-banner',
  templateUrl: './banner.component.html',
  styleUrls: ['./banner.component.css']
})
export class BannerComponent {

  constructor(private authenticationService: AuthenticationService, private router: Router) { }

  logout() {
    this.authenticationService.logout().subscribe({
      next: () => {
        this.authenticationService.logout();
      }
    });
    this.router.navigate(['login']);
  }

}
