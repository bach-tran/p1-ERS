import { Pipe, PipeTransform } from '@angular/core';
import { Reimbursement } from '../models/reimbursement';

@Pipe({
  name: 'reimbStatus'
})
export class ReimbStatusPipe implements PipeTransform {

  transform(value: Reimbursement[], statusType: string): Reimbursement[] {
    if (statusType === 'all') {
      return value;
    } else if (statusType === 'denied') {
      return value.filter((reimb) => reimb.status.status === 'DENIED');
    } else if (statusType === 'approved') {
      return value.filter((reimb) => reimb.status.status === 'APPROVED');
    } else if (statusType === 'pending') {
      return value.filter((reimb) => reimb.status.status === 'PENDING');
    }
  }

}
