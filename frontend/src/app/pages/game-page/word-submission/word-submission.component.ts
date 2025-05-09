import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { submitWord } from '../../../store/game-store/game-store.actions';

@Component({
  selector: 'app-word-submission',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './word-submission.component.html',
})
export class WordSubmissionComponent {
  form: FormGroup;
  roomId: string;

  constructor(private fb: FormBuilder, private store: Store) {
    this.form = this.fb.group({
      word: ['', [Validators.required, Validators.minLength(3)]]
    });
    this.roomId = window.location.pathname.split('/').pop()!;
  }

  submit(): void {
    if (this.form.invalid) return;
    const word = this.form.value.word!;
    this.store.dispatch(submitWord({ word, roomId: this.roomId }));
    this.form.disable();
  }
}
