import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Store } from '@ngrx/store';
import { WebSocketService } from '../../../service/web-socket.service';
import { selectCurrentDrawer } from '../../../store/game-store/game-store.selectors';
import { User } from '../../../api/model/user';
import { UserService } from '../../../api/services/user.service';

@Component({
  selector: 'app-guessing-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './guessing-component.component.html',
  styleUrls: ['./guessing-component.component.scss']
})
export class GuessingComponentComponent {
  guess: string = '';
  user: User | null = null;
  currentDrawerId: string | null = null;
  roomId: string = window.location.pathname.split('/').pop()!;
  chatMessages: { username: string; message: string; correctGuess?: boolean }[] = [];

  constructor(
    private store: Store,
    private wsService: WebSocketService,
    private userService: UserService
  ) {
    this.userService.getCurrentUser().subscribe(user => this.user = user);

    this.store.select(selectCurrentDrawer).subscribe(drawer => {
      this.currentDrawerId = drawer?.gameWebSocketSessionId ?? null;
    });

    this.wsService.chatReceived$.subscribe((msg: any) => {
      this.chatMessages.push(msg);
    });
  }

  sendGuess(): void {
    if (!this.guess.trim() || this.user?.gameWebSocketSessionId === this.currentDrawerId) return;

    this.wsService.sendGuess(this.roomId, this.guess.trim());
    this.guess = '';
  }
}
