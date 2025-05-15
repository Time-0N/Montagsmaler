import { Component } from '@angular/core';
import {Store} from '@ngrx/store';
import {leaveGame} from '../../../store/game-store/game-store.actions';

@Component({
  selector: 'app-leave-game-button',
  imports: [],
  templateUrl: './leave-game-button.component.html',
  styleUrl: './leave-game-button.component.scss'
})
export class LeaveGameButtonComponent {
  constructor(private store: Store) {}

  leaveGame(): void {
    const roomId = window.location.pathname.split('/').pop()!;
    this.store.dispatch(leaveGame({ roomId }));
  }
}
