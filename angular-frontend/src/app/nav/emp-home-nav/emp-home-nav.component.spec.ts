import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpHomeNavComponent } from './emp-home-nav.component';

describe('EmpHomeNavComponent', () => {
  let component: EmpHomeNavComponent;
  let fixture: ComponentFixture<EmpHomeNavComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmpHomeNavComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmpHomeNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
