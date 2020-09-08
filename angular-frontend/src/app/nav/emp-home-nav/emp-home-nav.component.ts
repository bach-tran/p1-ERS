import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/auth-service/authentication.service';

@Component({
  selector: 'app-emp-home-nav',
  templateUrl: './emp-home-nav.component.html',
  styleUrls: ['./emp-home-nav.component.css', '../nav.component.css']
})
export class EmpHomeNavComponent implements OnInit {

  isManager: boolean;

  constructor(private authService: AuthenticationService) {
    this.isManager = this.authService.getUser().role.role === 'MANAGER';
   }

  ngOnInit(): void {
  }

}
