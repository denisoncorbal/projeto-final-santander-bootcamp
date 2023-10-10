import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AppPageComponent } from './components/app-page/app-page.component';
import { AppTitleComponent } from './components/app-title/app-title.component';
import { BannerComponent } from './components/banner/banner.component';
import { FooterComponent } from './components/footer/footer.component';
import { AddIncomeComponent } from './pages/add-income/add-income.component';
import { AddOutcomeComponent } from './pages/add-outcome/add-outcome.component';
import { IncomeHistoryComponent } from './pages/income-history/income-history.component';
import { OutcomeHistoryComponent } from './pages/outcome-history/outcome-history.component';
import { TransactionHistoryComponent } from './pages/transaction-history/transaction-history.component';

@NgModule({
  declarations: [
    AppComponent,
    TransactionHistoryComponent,
    IncomeHistoryComponent,
    OutcomeHistoryComponent,
    AddIncomeComponent,
    AddOutcomeComponent,
    BannerComponent,
    AppTitleComponent,
    AppPageComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
