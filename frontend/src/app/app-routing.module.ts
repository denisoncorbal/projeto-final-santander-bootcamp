import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TransactionHistoryComponent } from './pages/transaction-history/transaction-history.component';
import { IncomeHistoryComponent } from './pages/income-history/income-history.component';
import { OutcomeHistoryComponent } from './pages/outcome-history/outcome-history.component';
import { AddIncomeComponent } from './pages/add-income/add-income.component';
import { AddOutcomeComponent } from './pages/add-outcome/add-outcome.component';

const routes: Routes = [
  {path: '', redirectTo: "/transaction-history", pathMatch: 'full'},
  {path: 'transaction-history', component: TransactionHistoryComponent},
  {path: 'income-history', component: IncomeHistoryComponent},
  {path: 'outcome-history', component: OutcomeHistoryComponent},
  {path: 'add-income', component: AddIncomeComponent},
  {path: 'add-outcome', component: AddOutcomeComponent},
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
