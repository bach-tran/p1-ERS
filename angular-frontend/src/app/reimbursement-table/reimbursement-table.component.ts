import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { ReimbursementService } from 'src/app/services/reimbursement-service/reimbursement.service';
import { Reimbursement } from 'src/app/models/reimbursement';
import { AuthenticationService } from 'src/app/services/auth-service/authentication.service';

@Component({
  selector: 'app-reimbursement-table',
  templateUrl: './reimbursement-table.component.html',
  styleUrls: ['./reimbursement-table.component.css']
})
export class ReimbursementTableComponent implements OnInit {

  public statusType: string;
  public reimbursements: Reimbursement[];
  public refreshing: boolean;

  constructor(private reimService: ReimbursementService, private authService: AuthenticationService, private router: Router) {
    this.statusType = 'all';
  }

  async ngOnInit(): Promise<void> {
    this.reimbursements = this.reimService.empReimbDataCache;
    await this.getReimbursementsUser();
  }

  async getReimbursementsUser(): Promise<void> {
    this.refreshing = true;
    if (await this.authService.checkAuthorization()) {
      const id = this.authService.getUser().id;

      this.reimbursements = await this.reimService.getUserReimbursements(id);

      this.refreshing = false;
    } else {
      alert('Login session expired.');
      this.router.navigate(['/login']);
    }
  }

}
