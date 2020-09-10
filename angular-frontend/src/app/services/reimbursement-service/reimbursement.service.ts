import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { Reimbursement } from 'src/app/models/reimbursement';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReimbursementService {

  // empReimbDataCache: Reimbursement[];
  // managerReimbDataCache: Reimbursement[];

  constructor(private http: HttpClient, private router: Router) { }

  public async getUserReimbursements(id: number): Promise<Reimbursement[]> {
    const response = this.http.get<Reimbursement[]>(environment.API_URL + ':' + environment.PORT + `/project1/reimb/user/${id}`, {
      withCredentials: true
    }).toPromise();

    (await response).sort((a, b) => b.id - a.id);

    // this.empReimbDataCache = await response;

    return response;
  }

  public async getReimbursements(): Promise<Reimbursement[]> {
    const response = this.http.get<Reimbursement[]>(environment.API_URL + ':' + environment.PORT + `/project1/reimb`, {
      withCredentials: true
    }).toPromise();

    (await response).sort((a, b) => b.id - a.id);

    // this.managerReimbDataCache = await response;

    return response;
  }

  public async approveReimbursement(id: number): Promise<void> {
    const response = this.http.put(environment.API_URL + ':' + environment.PORT + `/project1/reimb/${id}/?operation=approve`, {}, {
      withCredentials: true,
    }).toPromise();
  }

  public async denyReimbursement(id: number): Promise<void> {
    const response = this.http.put(environment.API_URL + ':' + environment.PORT + `/project1/reimb/${id}/?operation=deny`, {}, {
      withCredentials: true,
    }).toPromise();
  }

  public getReceipt(id: number): Observable<Blob> {
    const response = this.http.get(environment.API_URL + ':' + environment.PORT + `/project1/download/reimb/${id}`, {
      responseType: 'blob',
      withCredentials: true
    });

    return response;
  }
}
