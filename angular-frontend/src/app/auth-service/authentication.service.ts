import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../models/user';

@Injectable(
{providedIn: 'root'}
)
export class AuthenticationService {

  private currentUser: User;

  constructor(private http: HttpClient, private router: Router) { }

  public async login(user: string, pass: string): Promise<User> {
    try {
      const response: Promise<User> = this.http.post<User>('http://localhost:8080/project1/login', {
        username: user,
        password: pass
      }, {
        withCredentials: true
      }).toPromise();

      return response;
    } catch (error) {
      console.log(error);
      alert('Error logging in!');
    }
  }

  public async checkAuthorization(): Promise<boolean> {
    try {
      const response = await this.http.post<User>('http://localhost:8080/project1/login/check', {}, {
        withCredentials: true
      }).toPromise();
      this.setUser(response);

      return true;
    } catch (error) {
      return false;
    }
  }

  public setUser(user: User): void {
    this.currentUser = user;
  }

  public getUser(): User {
    return this.currentUser;
  }
}