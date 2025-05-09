import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { joinGame } from '../../../store/game-store/game-store.actions';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-join-game-form',
  standalone: true,
  templateUrl: './join-game-form.component.html',
  styleUrls: ['./join-game-form.component.scss'],
  imports: [CommonModule, FormsModule]
})
export class JoinGameFormComponent {
  roomId = '';

  constructor(private store: Store) {}

  join(): void {
    if (this.roomId.trim()) {
      this.store.dispatch(joinGame({ roomId: this.roomId.trim() }));
    }
  }
}
