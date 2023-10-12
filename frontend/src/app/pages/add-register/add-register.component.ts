import { Component, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Register } from 'src/app/model/register';
import { RegisterClass } from 'src/app/model/register-class';
import { RegisterUser } from 'src/app/model/register-user';
import { BackendDataService } from 'src/app/services/backend-data.service';

@Component({
  selector: 'app-add-register',
  templateUrl: './add-register.component.html',
  styleUrls: ['./add-register.component.css']
})
export class AddRegisterComponent implements OnInit{
  constructor(private backendService: BackendDataService){}  
  
  @Input()
  registerUser: RegisterUser = {
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    registers: []
  };

  @Input()
  registerClass: RegisterClass[] | null = null;

  ngOnInit(): void {
      this.backendService.readUsers().subscribe({
        next: (value) => {
          this.registerUser = value[0];
        }
      });
      this.backendService.readClasses().subscribe({
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
        this.backendService.associateRegister(value.id?value.id:-1, f.value.registerUserSelect.email, f.value.registerClassSelect.name).subscribe();
      }
    });
  }
}
