import { Component, OnInit } from '@angular/core';
import { RegisterUser } from 'src/app/model/register-user';
import { BackendDataService } from 'src/app/services/backend-data.service';

@Component({
  selector: 'app-transaction-history',
  templateUrl: './transaction-history.component.html',
  styleUrls: ['./transaction-history.component.css']
})
export class TransactionHistoryComponent implements OnInit{

  constructor(private backendService: BackendDataService){}

  private fakeUser: RegisterUser = {
    firstName: 'Denison',
    lastName: 'Corbal',
    email: 'denison.corbal@gmail.com',
    password: '123456',
    registers: []
  };

  ngOnInit(): void {
      this.backendService.createUser(this.fakeUser).subscribe({
        next: (value)=>{console.log(value)},
        error: (error)=>{console.log(error)},
        complete: ()=>{console.log("Complete")}
      });
  }
}
