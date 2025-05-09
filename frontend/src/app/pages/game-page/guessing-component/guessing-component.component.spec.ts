import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuessingComponentComponent } from './guessing-component.component';

describe('GuessingComponentComponent', () => {
  let component: GuessingComponentComponent;
  let fixture: ComponentFixture<GuessingComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuessingComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GuessingComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
