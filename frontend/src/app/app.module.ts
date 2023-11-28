import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AppPageComponent } from './components/app-page/app-page.component';
import { AppTitleComponent } from './components/app-title/app-title.component';
import { BannerComponent } from './components/banner/banner.component';
import { FloatMessageComponent } from './components/float-message/float-message.component';
import { FooterComponent } from './components/footer/footer.component';
import { JwtInterceptor } from './interceptors/jwt.interceptor';
import { AddClassComponent } from './pages/add-class/add-class.component';
import { AddRegisterComponent } from './pages/add-register/add-register.component';
import { AddUserComponent } from './pages/add-user/add-user.component';
import { IncomeHistoryComponent } from './pages/income-history/income-history.component';
import { LoginComponent } from './pages/login/login.component';
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
    FooterComponent,
    LoginComponent,
    AddUserComponent,
    AddClassComponent,
    FloatMessageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
