import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { createGame } from '../../../store/game-store/game-store.actions';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-game-button',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule
  ],
  templateUrl: './create-game-button.component.html',
  styleUrls: ['./create-game-button.component.scss']
})
export class CreateGameButtonComponent {
  constructor(private store: Store) {}

  create(): void {
    console.log('[Component] Dispatching createGame');
    this.store.dispatch(createGame());
  }
}
