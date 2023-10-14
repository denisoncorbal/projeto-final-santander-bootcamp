import { Component, Input, OnInit } from '@angular/core';
import { Register } from 'src/app/model/register';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { BackendDataService } from 'src/app/services/backend-data.service';

@Component({
  selector: 'app-transaction-history',
  templateUrl: './transaction-history.component.html',
  styleUrls: ['./transaction-history.component.css']
})
export class TransactionHistoryComponent implements OnInit{

  constructor(private backendService: BackendDataService, private authenticationService: AuthenticationService) { }
  
  registers: Register[] = [];
  balanceValue: number = 0.0;
  incomeValue: number = 0.0;
  outcomeValue: number = 0.0;

  ngOnInit(): void {
      this.backendService.readRegistersByUser(this.authenticationService.getActualEmail()).subscribe({
        next: (value)=>{
          this.registers = value;
          this.calculeteAllBalances();
        }
      })
      
  }

  deleteById(position: number){
    const id = this.registers[position].id;
    this.backendService.deleteRegister(id ? id : -1).subscribe();
    this.registers.splice(position, 1);
    this.calculeteAllBalances();
  }

  private calculeteAllBalances(){
    this.balanceValue = this.registers.reduce((accumulator: number, current: Register)=>
      current.type == "INCOME" ? accumulator + current.registerValue : accumulator - current.registerValue, 0
    );
    this.incomeValue = this.registers.reduce((accumulator: number, current: Register)=>
      current.type == "INCOME" ? accumulator + current.registerValue : accumulator, 0
    );
    this.outcomeValue = this.registers.reduce((accumulator: number, current: Register)=>
      current.type == "OUTCOME" ? accumulator + current.registerValue : accumulator, 0
    );
  }

}
