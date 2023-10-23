import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { RegisterClass } from 'src/app/model/register-class';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { BackendDataService } from 'src/app/services/backend-data.service';

@Component({
  selector: 'app-add-class',
  templateUrl: './add-class.component.html',
  styleUrls: ['./add-class.component.css']
})
export class AddClassComponent {

  constructor(private backendService: BackendDataService, private authenticationService: AuthenticationService, private router: Router){}

  registerClass: RegisterClass = {
    name: '',
    registers: []
  }

  addClass(f: NgForm){
    this.registerClass = {
      name: f.value.name,
      registers: []
    }
    this.backendService.createClass(this.registerClass).subscribe({
      next: (value)=>{
        this.backendService.associateClass(value.id?value.id:-1, this.authenticationService.getActualEmail()).subscribe({
          complete: () =>{
            this.router.navigate(["add-transaction"]);
          }
        });
      }
    });
    
  }

}
