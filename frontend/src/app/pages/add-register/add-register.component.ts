import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Register } from 'src/app/model/register';
import { RegisterClass } from 'src/app/model/register-class';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { BackendDataService } from 'src/app/services/backend-data.service';

@Component({
  selector: 'app-add-register',
  templateUrl: './add-register.component.html',
  styleUrls: ['./add-register.component.css']
})
export class AddRegisterComponent implements OnInit {
  constructor(private backendService: BackendDataService, private authenticationService: AuthenticationService, private router: Router, private formBuilder: FormBuilder) { }

  protected messageData = {
    name: '',
    type: '',
    action: '',
    showMessage: false
  };

  protected formAddRegister = this.formBuilder.group({
    date: [new Date(), Validators.required],
    registerValue: [0, Validators.required],
    registerType: ['', Validators.required],
    registerClassName: [{} as RegisterClass, Validators.required]
  })

  @Input()
  registerClass: RegisterClass[] | null = null;

  ngOnInit(): void {
    this.backendService.readClassesByUser(this.authenticationService.getActualEmail()).subscribe({
      next: (value) => {
        this.registerClass = value;
      }
    })
  }

  onSubmit() {
    const register: Register = {
      date: this.formAddRegister.controls.date.value!,
      registerValue: this.formAddRegister.controls.registerValue.value!,
      type: this.formAddRegister.controls.registerType.value!,
      registerUser: null,
      registerClass: null
    }
    this.backendService.createRegister(register).subscribe({
      next: (value) => {
        this.backendService.associateRegister(value.id!, this.authenticationService.getActualEmail(), this.formAddRegister.controls.registerClassName.value!.name!).subscribe({
          next: ()=>{this.showMessage('success')},
          error: ()=>{this.showMessage('failure')}
        });
      }
    });
  }

  addClass() {
    this.router.navigate(["add-class"]);
  }

  showMessage(type: string) {
    this.messageData.type = type;
    this.messageData.action = 'create';
    this.messageData.name = 'transaction';
    this.messageData.showMessage = true;
    setTimeout(() => { this.messageData.showMessage = false }, 3000);
  }
}
