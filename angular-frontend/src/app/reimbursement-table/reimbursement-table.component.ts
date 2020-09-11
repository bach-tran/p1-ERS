import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { ReimbursementService } from 'src/app/services/reimbursement-service/reimbursement.service';
import { Reimbursement } from 'src/app/models/reimbursement';
import { AuthenticationService } from 'src/app/services/auth-service/authentication.service';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-reimbursement-table',
  templateUrl: './reimbursement-table.component.html',
  styleUrls: ['./reimbursement-table.component.css']
})
export class ReimbursementTableComponent implements OnInit {

  public statusType: string;
  public reimbursements: Reimbursement[];
  public refreshing: boolean;

  viewedReceiptId: number;
  viewedReceiptAuthor: string;
  imageToShow: any;
  isImageLoading: boolean;
  mySrc;

  constructor(private reimService: ReimbursementService, private authService: AuthenticationService, private router: Router) {
    this.statusType = 'all';
  }

  async ngOnInit(): Promise<void> {
    // this.reimbursements = this.reimService.empReimbDataCache;
    await this.getReimbursementsUser();
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

  getImage(id: number, name: string): void {
    this.viewedReceiptId = id;
    this.viewedReceiptAuthor = name;

    this.isImageLoading = true;
    this.reimService.getReceipt(id).subscribe(data => {
      this.createImageFromBlob(data);
      this.isImageLoading = false;
    }, error => {
      this.isImageLoading = false;
      console.log(error);
    });

  }

  createImageFromBlob(image: Blob) {
    const reader = new FileReader();
    reader.addEventListener('load', () => {
       this.imageToShow = reader.result;
    }, false);

    if (image) {
       reader.readAsDataURL(image);
    }
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
