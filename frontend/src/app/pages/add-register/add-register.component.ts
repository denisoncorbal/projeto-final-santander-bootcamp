import { Component, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Register } from 'src/app/model/register';
import { RegisterClass } from 'src/app/model/register-class';
import { RegisterUser } from 'src/app/model/register-user';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { BackendDataService } from 'src/app/services/backend-data.service';

@Component({
  selector: 'app-add-register',
  templateUrl: './add-register.component.html',
  styleUrls: ['./add-register.component.css']
})
export class AddRegisterComponent implements OnInit{
  constructor(private backendService: BackendDataService, private authenticationService:AuthenticationService, private router: Router){}    

  @Input()
  registerClass: RegisterClass[] | null = null;

  ngOnInit(): void {      
      this.backendService.readClassesByUser(this.authenticationService.getActualEmail()).subscribe({
        next: (value)=>{
          this.registerClass = value;
        }
      })
  }

  onSubmit(f: NgForm){        
    const register: Register = {
      date: f.value.date,
      registerValue: f.value.registerValue,
      type: f.value.registerTypeSelect,
      registerUser: null,
      registerClass: null
    }    
    this.backendService.createRegister(register).subscribe({
      next: (value)=>{
        this.backendService.associateRegister(value.id?value.id:-1, this.authenticationService.getActualEmail(), f.value.registerClassSelect.name).subscribe();
      }
    });
  }

  addClass(){
    this.router.navigate(["add-class"]);
  }
}
