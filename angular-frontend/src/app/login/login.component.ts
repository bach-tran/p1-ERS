import { Role } from './../models/role';
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { User } from '../models/user';
import { AuthenticationService } from '../services/auth-service/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string;
  password: string;

  constructor(private authService: AuthenticationService, private http: HttpClient, private router: Router) {
  }

  async ngOnInit(): Promise<void> {
    if (await this.authService.checkAuthorization()) {
      const user: User = this.authService.getUser();

      this.navigateRole(user);

    }
  }

  async onClick(): Promise<void> {
    try {
      const user: User = await this.authService.login(this.username, this.password);

      await this.navigateRole(user);

    } catch (error) {
      alert('Error logging in');
    }

  }

  private async navigateRole(user: User): Promise<void> {
    if (user.role.role === 'EMPLOYEE') {
      this.router.navigate(['employee-home']);
    } else if (user.role.role === 'MANAGER') {
      this.router.navigate(['manager-home']);
    } else {
      alert('Unknown User Role, cancelling session');
      await this.authService.logout();
    }
  }

}
