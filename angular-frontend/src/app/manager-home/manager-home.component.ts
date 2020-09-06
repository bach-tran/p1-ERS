import { Router } from '@angular/router';
import { AuthenticationService } from './../auth-service/authentication.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-manager-home',
  templateUrl: './manager-home.component.html',
  styleUrls: ['./manager-home.component.css']
})
export class ManagerHomeComponent implements OnInit {

  constructor(private authService: AuthenticationService, private router: Router) { }

  async ngOnInit(): Promise<void> {
    if (!(await this.authService.checkAuthorization())) {
      alert('You have been signed out.');
      this.router.navigate(['login']);
    }
  }

}
