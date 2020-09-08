import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReimbursementTableManagerComponent } from './reimbursement-table-manager.component';

describe('ReimbursementTableManagerComponent', () => {
  let component: ReimbursementTableManagerComponent;
  let fixture: ComponentFixture<ReimbursementTableManagerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReimbursementTableManagerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReimbursementTableManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
