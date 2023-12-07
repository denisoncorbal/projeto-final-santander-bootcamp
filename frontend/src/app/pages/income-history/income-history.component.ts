import { Component } from '@angular/core';
import { Register } from 'src/app/model/register';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { BackendDataService } from 'src/app/services/backend-data.service';

@Component({
  selector: 'app-income-history',
  templateUrl: './income-history.component.html',
  styleUrls: ['./income-history.component.css']
})
export class IncomeHistoryComponent {
  constructor(private backendService: BackendDataService, private authenticationService: AuthenticationService){}

  registers: Register[] = [];  
  incomeValue: number = 0.0;

  ngOnInit(): void {
      this.backendService.readRegistersByUserAndType(this.authenticationService.getActualEmail(), 'INCOME').subscribe({
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
    this.incomeValue = this.registers.reduce((accumulator: number, current: Register)=>
      accumulator + current.registerValue, 0
    );   
  }
}
