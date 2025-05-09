import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {GameLobbyComponent} from '../game-lobby/game-lobby.component';
import {WordSubmissionComponent} from '../word-submission/word-submission.component';
import {Store} from '@ngrx/store';
import {ActivatedRoute} from '@angular/router';
import {WebSocketService} from '../../../service/web-socket.service';
import {ResultComponentComponent} from '../result-component/result-component.component';
import {GuessingComponentComponent} from '../guessing-component/guessing-component.component';
import {Observable} from 'rxjs';
import {DrawingComponentComponent} from '../drawing-component/drawing-component.component';
import {selectGameSession} from '../../../store/game-store/game-store.selectors';

@Component({
  selector: 'app-game-page',
  standalone: true,
  imports: [
    CommonModule,
    GameLobbyComponent,
    WordSubmissionComponent,
    DrawingComponentComponent,
    GuessingComponentComponent,
    ResultComponentComponent,
    DrawingComponentComponent
  ],
  templateUrl: './game-page.component.html',
})
export class GamePageComponent implements OnInit {
  session$: Observable<any> | null | undefined
  roomId: string | null = null;

  constructor(
    private store: Store,
    private route: ActivatedRoute,
    private wsService: WebSocketService
  ) {}

  ngOnInit(): void {
    this.session$ = this.store.select(selectGameSession);

    this.roomId = this.route.snapshot.paramMap.get('roomId');
    if (this.roomId) {
      this.wsService.connect(this.roomId);
    }
  }
}
