import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authenticationGuard } from './authentication.guard';
import { AddClassComponent } from './pages/add-class/add-class.component';
import { AddRegisterComponent } from './pages/add-register/add-register.component';
import { AddUserComponent } from './pages/add-user/add-user.component';
import { IncomeHistoryComponent } from './pages/income-history/income-history.component';
import { LoginComponent } from './pages/login/login.component';
import { OutcomeHistoryComponent } from './pages/outcome-history/outcome-history.component';
import { TransactionHistoryComponent } from './pages/transaction-history/transaction-history.component';

const routes: Routes = [
  {path: '', redirectTo: "/transaction-history", pathMatch: 'full'},
  {path: 'transaction-history', component: TransactionHistoryComponent, canActivate: [authenticationGuard]},
  {path: 'income-history', component: IncomeHistoryComponent, canActivate: [authenticationGuard]},
  {path: 'outcome-history', component: OutcomeHistoryComponent, canActivate: [authenticationGuard]},
  {path: 'add-transaction', component: AddRegisterComponent, canActivate: [authenticationGuard]},
  {path: 'add-user', component: AddUserComponent, canActivate:[authenticationGuard]},
  {path: 'add-class', component: AddClassComponent, canActivate:[authenticationGuard]},
  {path: 'login', component: LoginComponent},
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
