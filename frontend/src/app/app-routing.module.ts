import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddRegisterComponent } from './pages/add-register/add-register.component';
import { IncomeHistoryComponent } from './pages/income-history/income-history.component';
import { OutcomeHistoryComponent } from './pages/outcome-history/outcome-history.component';
import { TransactionHistoryComponent } from './pages/transaction-history/transaction-history.component';

const routes: Routes = [
  {path: '', redirectTo: "/transaction-history", pathMatch: 'full'},
  {path: 'transaction-history', component: TransactionHistoryComponent},
  {path: 'income-history', component: IncomeHistoryComponent},
  {path: 'outcome-history', component: OutcomeHistoryComponent},
  {path: 'add-transaction', component: AddRegisterComponent}
];

@NgModule({  
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
