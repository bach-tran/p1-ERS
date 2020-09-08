import { Router } from '@angular/router';
import { AuthenticationService } from '../services/auth-service/authentication.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  constructor(private authService: AuthenticationService, private router: Router) { }

  async ngOnInit(): Promise<void> {
    await this.authService.logout();
    this.authService.setUser(undefined);
    this.router.navigate(['login']);
  }

}
