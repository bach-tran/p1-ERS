import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string;
  password: string;

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
    // if (sessionStorage.getItem('username') !== null) {
    //   this.router.navigate(['employee-home']);
    // }
  }

  async onClick(): Promise<void> {
    const loginData = {
      username: this.username,
      password: this.password
    };

    try {
      await this.http.post('http://localhost:8080/project1/login', loginData, {
        withCredentials: true
      }).toPromise();

      sessionStorage.setItem('username', loginData.username);

      this.router.navigate(['employee-home']);
    } catch (error) {
      console.log(error);
      alert('Error logging in!');
    }

  }

}
