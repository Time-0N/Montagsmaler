import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateGameButtonComponent } from './create-game-button.component';

describe('CreateGameButtonComponent', () => {
  let component: CreateGameButtonComponent;
  let fixture: ComponentFixture<CreateGameButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateGameButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateGameButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
