import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeaveGameButtonComponent } from './leave-game-button.component';

describe('LeaveGameButtonComponent', () => {
  let component: LeaveGameButtonComponent;
  let fixture: ComponentFixture<LeaveGameButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LeaveGameButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LeaveGameButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
