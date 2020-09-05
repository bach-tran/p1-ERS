import { Router } from '@angular/router';
import { AuthenticationService } from '../auth-service/authentication.service';
import { Component, OnInit } from '@angular/core';
import { User } from '../models/user';

@Component({
  selector: 'app-employee-home',
  templateUrl: './employee-home.component.html',
  styleUrls: ['./employee-home.component.css']
})
export class EmployeeHomeComponent implements OnInit {

  public fullName: string;

  constructor(private authService: AuthenticationService, private router: Router) {}

  async ngOnInit(): Promise<void> {
    if (!(await this.authService.checkAuthorization())) {
      alert('You have been signed out.')
      this.router.navigate(['login']);
    }

    console.log(this.authService.getUser());
    this.fullName = this.authService.getUser().firstName + ' ' + this.authService.getUser().lastName;
  }

}
