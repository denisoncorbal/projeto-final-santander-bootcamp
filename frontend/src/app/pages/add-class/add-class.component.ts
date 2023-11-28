import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
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

  constructor(private backendService: BackendDataService, private authenticationService: AuthenticationService, private router: Router, private formBuilder: FormBuilder) { }

  protected messageData = {
    name: '',
    type: '',
    action: '',
    showMessage: false
  };

  protected formAddClass = this.formBuilder.group({
    name: ['', Validators.required]
  })

  registerClass: RegisterClass = {
    name: '',
    registers: []
  }

  addClass() {
    this.registerClass = {
      name: this.formAddClass.controls.name.value!,
      registers: []
    }
    this.backendService.createClass(this.registerClass).subscribe({
      next: (value) => {
        this.backendService.associateClass(value.id!, this.authenticationService.getActualEmail()).subscribe({
          next: () => { this.showMessage('success')},
          error: ()=>{this.showMessage('failure')},
        });
      }
    });
  }

  cancel(){
    this.router.navigate(["add-transaction"])
  }

  showMessage(type: string) {
    this.messageData.type = type;
    this.messageData.action = 'create';
    this.messageData.name = 'class';
    this.messageData.showMessage = true;
    setTimeout(() => { this.messageData.showMessage = false }, 3000);
  }

}