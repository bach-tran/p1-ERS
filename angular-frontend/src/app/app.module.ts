import { AuthenticationService } from './auth-service/authentication.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavComponent } from './nav/nav.component';
import { HomeNavComponent } from './nav/home-nav/home-nav.component';
import { HomeComponent } from './home/home.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { LoginComponent } from './login/login.component';
import { LoginNavComponent } from './nav/login-nav/login-nav.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { EmployeeHomeComponent } from './employee-home/employee-home.component';
import { EmpHomeNavComponent } from './nav/emp-home-nav/emp-home-nav.component';
import { LogoutComponent } from './logout/logout.component';
import { ManagerHomeComponent } from './manager-home/manager-home.component';
import { ManagerHomeNavComponent } from './nav/manager-home-nav/manager-home-nav.component';

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    HomeNavComponent,
    HomeComponent,
    NotFoundComponent,
    LoginComponent,
    LoginNavComponent,
    EmployeeHomeComponent,
    EmpHomeNavComponent,
    LogoutComponent,
    ManagerHomeComponent,
    ManagerHomeNavComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [AuthenticationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
