import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/auth-service/authentication.service';

@Component({
  selector: 'app-emp-home-nav',
  templateUrl: './emp-home-nav.component.html',
  styleUrls: ['./emp-home-nav.component.css', '../nav.component.css']
})
export class EmpHomeNavComponent implements OnInit {

  isManager: boolean;

  constructor(private authService: AuthenticationService, private router: Router) { }

  async ngOnInit(): Promise<void> {
    if (!(await this.authService.checkAuthorization())) {
      alert('Login session expired.');
      this.router.navigate(['login']);
    }

    this.isManager = this.authService.getUser().role.role === 'MANAGER';
  }

}
