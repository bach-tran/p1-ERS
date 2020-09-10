import { AuthenticationService } from '../services/auth-service/authentication.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-employee-submit',
  templateUrl: './employee-submit.component.html',
  styleUrls: ['./employee-submit.component.css']
})
export class EmployeeSubmitComponent implements OnInit {

  selectedFile = null;
  amount: number;
  description: string;
  type: number;

  constructor(private http: HttpClient, private router: Router, private authService: AuthenticationService) { }

  async ngOnInit(): Promise<void> {
    if (!(await this.authService.checkAuthorization())) {
      alert('Login session expired.');
      this.router.navigate(['login']);
    }
  }

  onFileSelected(event): void {
    this.selectedFile = event.target.files[0] as File;
  }

  async onUpload(): Promise<void> {
    if (!(await this.authService.checkAuthorization())) {

      alert('Login session expired.');
      this.router.navigate(['login']);

    } else {

      try {
        const fd = new FormData();
        fd.append('amount', this.amount.toString());
        fd.append('description', this.description);
        fd.append('type', this.type.toString());
        fd.append('file', this.selectedFile, this.selectedFile.name);

        await this.http.post(environment.API_URL + ':' + environment.PORT + `/project1/reimb`, fd, {
          withCredentials: true
        }).toPromise();

        this.router.navigate(['employee-home']);

      } catch (error) {
        console.log(error);
        alert('Error when attempting to submit, please make sure values are correct');
      }
    }

  }


  checkAmount(): void {
    let numberDecimals: number;

    if (Math.floor(this.amount) === this.amount) {
      numberDecimals = 0;
    } else {
      numberDecimals = this.amount.toString().split('.')[1].length || 0;
    }

    if (numberDecimals > 2) {
      this.amount = parseFloat((Math.floor(this.amount * 100) / 100).toFixed(2));
    }

  }

  selected(): void {
  }
}
