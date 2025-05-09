import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WordSubmissionComponent } from './word-submission.component';

describe('WordSubmissionComponent', () => {
  let component: WordSubmissionComponent;
  let fixture: ComponentFixture<WordSubmissionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WordSubmissionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WordSubmissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
