import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { FormsModule } from "@angular/forms";
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AppPageComponent } from './components/app-page/app-page.component';
import { AppTitleComponent } from './components/app-title/app-title.component';
import { BannerComponent } from './components/banner/banner.component';
import { FooterComponent } from './components/footer/footer.component';
import { AddRegisterComponent } from './pages/add-register/add-register.component';
import { IncomeHistoryComponent } from './pages/income-history/income-history.component';
import { OutcomeHistoryComponent } from './pages/outcome-history/outcome-history.component';
import { TransactionHistoryComponent } from './pages/transaction-history/transaction-history.component';

@NgModule({
  declarations: [
    AppComponent,
    TransactionHistoryComponent,
    IncomeHistoryComponent,
    OutcomeHistoryComponent,
    AddRegisterComponent,
    BannerComponent,
    AppTitleComponent,
    AppPageComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
