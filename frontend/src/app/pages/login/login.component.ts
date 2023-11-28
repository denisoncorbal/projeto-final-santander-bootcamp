import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  constructor(private router: Router, private authenticationService: AuthenticationService, private formBuilder: FormBuilder) { }

  protected messageData = {
    name: '',
    type: '',
    action: '',
    showMessage: false
  };

  protected formLogin = this.formBuilder.group({
    email: ['', [Validators.email, Validators.required]],
    password: ['', [Validators.pattern(/^[a-zA-Z0-9]{6,12}$/), Validators.required]]
  })

  login() {
    this.authenticationService.tryAthenticate(
      this.formLogin.controls.email.value!, this.formLogin.controls.password.value!
    ).add(() => {
      this.router.navigate(['']);
    });
  }

  createUser() {
    this.authenticationService.createUser({
      firstName: '',
      lastName: '',
      email: this.formLogin.controls.email.value!,
      password: this.formLogin.controls.password.value!,
      registers: []
    }).subscribe({
      next: () => { this.showMessage('success') },
      error: () => { this.showMessage('failure') }
    });
  }

  showMessage(type: string) {
    this.messageData.type = type;
    this.messageData.action = 'create';
    this.messageData.name = 'user';
    this.messageData.showMessage = true;
    setTimeout(() => { this.messageData.showMessage = false }, 3000);
  }

}
