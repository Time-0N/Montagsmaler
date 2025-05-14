import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { createGame } from '../../../store/game-store/game-store.actions';

@Component({
  selector: 'app-create-game-button',
  standalone: true,
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
