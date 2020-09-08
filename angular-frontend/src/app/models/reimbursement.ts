import { Time } from '@angular/common';
import { User } from './user';
import { Status } from './status';
import { Type } from './type';

export class Reimbursement {
  public id: number;
  public amount: number;
  public submitted: Time;
  public resolved: Time;
  public description: string;
  public author: User;
  public resolver: User;
  public status: Status;
  public type: Type;
}
