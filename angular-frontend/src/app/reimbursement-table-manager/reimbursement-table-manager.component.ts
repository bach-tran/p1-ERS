import { saveAs } from 'file-saver';
import { AuthenticationService } from '../services/auth-service/authentication.service';
import { Router } from '@angular/router';
import { ReimbursementService } from 'src/app/services/reimbursement-service/reimbursement.service';
import { Component, OnInit } from '@angular/core';
import { Reimbursement } from '../models/reimbursement';
import { I18nSelectPipe } from '@angular/common';

@Component({
  selector: 'app-reimbursement-table-manager',
  templateUrl: './reimbursement-table-manager.component.html',
  styleUrls: ['./reimbursement-table-manager.component.css']
})
export class ReimbursementTableManagerComponent implements OnInit {

  refreshing: boolean;
  reimbursements: Reimbursement[];
  statusType: string = 'all';

  constructor(private reimService: ReimbursementService, private router: Router, private authService: AuthenticationService) { }

  ngOnInit(): void {
    // this.reimbursements = this.reimService.managerReimbDataCache;

    this.getReimbursements();
  }

  downloadReceipt(id: number): void {
    console.log('downloading receipt id ' + id);
    this.reimService.getReceipt(id).subscribe(data => {
      if (data.type === 'image/jpeg') {
        saveAs(data, `receipt_${id}.jpeg`);
      } else if (data.type === 'image/png') {
        saveAs(data, `receipt_${id}.png`);
      } else if (data.type === 'image/gif') {
        saveAs(data, `receipt_${id}.gif`);
      }
    });
  }

  async getReimbursements(): Promise<void> {
    this.refreshing = true;
    if (await this.authService.checkAuthorization()) {
      this.reimbursements = await this.reimService.getReimbursements();

      this.refreshing = false;
    } else {
      alert('Login session expired.');
      this.router.navigate(['/login']);
    }
  }

  async approveReimbursement(id: number): Promise<void> {
    console.log('Approving ID ' + id);
    if (await this.authService.checkAuthorization()) {
      await this.reimService.approveReimbursement(id);

      this.sleep(1000);

      this.getReimbursements();
    } else {
      alert('Login session expired.');
      this.router.navigate(['/login']);
    }
  }

  async denyReimbursement(id: number): Promise<void> {
    console.log('Denying ID ' + id);
    if (await this.authService.checkAuthorization()) {
      await this.reimService.denyReimbursement(id);

      this.sleep(1000);

      this.getReimbursements();
    } else {
      alert('Login session expired.');
      this.router.navigate(['/login']);
    }
  }

  sleep(milliseconds): void {
    const date = Date.now();
    let currentDate = null;
    do {
      currentDate = Date.now();
    } while (currentDate - date < milliseconds);
  }

}
