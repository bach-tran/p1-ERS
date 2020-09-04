import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { User } from '../models/user';
import { AuthenticationService } from '../auth-service/authentication.service';

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
      this.router.navigate(['employee-home']);
    }
  }

  async onClick(): Promise<void> {
    try {
      const user: User = await this.authService.login(this.username, this.password);

      if (user.role === 'EMPLOYEE') {
        this.router.navigate(['employee-home']);
      } else {
        console.log('You are not of the EMPLOYEE type');
      }

    } catch (error) {
      alert('Error logging in');
    }

  }

}
