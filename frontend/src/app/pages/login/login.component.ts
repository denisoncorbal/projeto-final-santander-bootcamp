import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  constructor(private router:Router, private authenticationService: AuthenticationService){}

  login(f: NgForm){
    this.authenticationService.tryAthenticate(f.value.email, f.value.password).add(()=>{
      this.router.navigate(['']);
    });
  }

  createUser(f: NgForm){
    this.authenticationService.createUser({
      firstName: '',
      lastName: '',
      email: f.value.email,
      password: f.value.password,
      registers: []
    }).subscribe();
  }

}
