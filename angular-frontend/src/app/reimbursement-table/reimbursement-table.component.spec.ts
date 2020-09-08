import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReimbursementTableComponent } from './reimbursement-table.component';

describe('ReimbursementTableComponent', () => {
  let component: ReimbursementTableComponent;
  let fixture: ComponentFixture<ReimbursementTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReimbursementTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReimbursementTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
