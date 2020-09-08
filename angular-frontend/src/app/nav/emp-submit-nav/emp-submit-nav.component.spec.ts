import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpSubmitNavComponent } from './emp-submit-nav.component';

describe('EmpSubmitNavComponent', () => {
  let component: EmpSubmitNavComponent;
  let fixture: ComponentFixture<EmpSubmitNavComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmpSubmitNavComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmpSubmitNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
