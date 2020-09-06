import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerHomeNavComponent } from './manager-home-nav.component';

describe('ManagerHomeNavComponent', () => {
  let component: ManagerHomeNavComponent;
  let fixture: ComponentFixture<ManagerHomeNavComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManagerHomeNavComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManagerHomeNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
