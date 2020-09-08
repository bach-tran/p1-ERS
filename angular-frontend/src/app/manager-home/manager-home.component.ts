import { Router } from '@angular/router';
import { AuthenticationService } from '../services/auth-service/authentication.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-manager-home',
  templateUrl: './manager-home.component.html',
  styleUrls: ['./manager-home.component.css']
})
export class ManagerHomeComponent implements OnInit {

  fullName: string;

  constructor(private authService: AuthenticationService, private router: Router) { }

  async ngOnInit(): Promise<void> {
    if (!(await this.authService.checkAuthorization())) {
      alert('Login session expired.');
      this.router.navigate(['login']);
    } else if (this.authService.getUser().role.role === 'EMPLOYEE') {
      alert('User not authorized');
      this.router.navigate(['login']);
    }

    this.fullName = this.authService.getUser().firstName + ' ' + this.authService.getUser().lastName;
  }

}
