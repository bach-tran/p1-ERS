import { EmployeeSubmitComponent } from './employee-submit/employee-submit.component';
import { ManagerHomeComponent } from './manager-home/manager-home.component';
import { LogoutComponent } from './logout/logout.component';
import { LoginComponent } from './login/login.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { HomeComponent } from './home/home.component';
import { AppComponent } from './app.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { EmployeeHomeComponent } from './employee-home/employee-home.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'logout', component: LogoutComponent},
  {path: 'employee-home', component: EmployeeHomeComponent},
  {path: 'manager-home', component: ManagerHomeComponent},
  {path: 'employee-submit', component: EmployeeSubmitComponent},
  {path: '**', component: NotFoundComponent, pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
