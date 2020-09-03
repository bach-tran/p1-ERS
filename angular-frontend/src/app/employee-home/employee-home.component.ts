import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-employee-home',
  templateUrl: './employee-home.component.html',
  styleUrls: ['./employee-home.component.css']
})
export class EmployeeHomeComponent implements OnInit {

  public username: string;

  constructor() {
    this.username = sessionStorage.getItem('username');
  }

  ngOnInit(): void {
  }

}
