import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Reimbursement } from 'src/app/models/reimbursement';

@Injectable({
  providedIn: 'root'
})
export class ReimbursementService {

  constructor(private http: HttpClient, private router: Router) { }

  public async getUserReimbursements(id: number): Promise<Reimbursement[]> {
    const response = this.http.get<Reimbursement[]>(`http://localhost:8080/project1/reimb/user/${id}`, {
      withCredentials: true
    }).toPromise();

    (await response).sort((a, b) => b.id - a.id);

    return response;
  }
}
