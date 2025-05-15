import { Component } from '@angular/core';
import {Store} from '@ngrx/store';
import {leaveGame} from '../../../store/game-store/game-store.actions';
import {WebSocketService} from '../../../service/web-socket.service';

@Component({
  selector: 'app-leave-game-button',
  imports: [],
  templateUrl: './leave-game-button.component.html',
  styleUrl: './leave-game-button.component.scss'
})
export class LeaveGameButtonComponent {
  constructor(
    private store: Store,
    private webSocketService: WebSocketService
  ) {}

  leaveGame(): void {
    const roomId = window.location.pathname.split('/').pop()!;
    this.webSocketService.disconnect();
    this.store.dispatch(leaveGame({ roomId }));
  }
}
