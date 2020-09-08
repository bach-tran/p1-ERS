import { ReimbursementService } from './services/reimbursement-service/reimbursement.service';
import { AuthenticationService } from './services/auth-service/authentication.service';
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
import { ReimbStatusPipe } from './pipes/reimb-status.pipe';
import { ReimbursementTableComponent } from './reimbursement-table/reimbursement-table.component';
import { EmployeeSubmitComponent } from './employee-submit/employee-submit.component';
import { EmpSubmitNavComponent } from './nav/emp-submit-nav/emp-submit-nav.component';
import { ReimbursementTableManagerComponent } from './reimbursement-table-manager/reimbursement-table-manager.component';

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
    ManagerHomeNavComponent,
    ReimbStatusPipe,
    ReimbursementTableComponent,
    EmployeeSubmitComponent,
    EmpSubmitNavComponent,
    ReimbursementTableManagerComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [AuthenticationService, ReimbursementService],
  bootstrap: [AppComponent]
})
export class AppModule { }
