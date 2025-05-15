import { Injectable } from '@angular/core';
import { Client, IMessage, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Store } from '@ngrx/store';
import { GameSession } from '../generated/model/gameSession';
import * as GameActions from '../store/game-store/game-store.actions';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient: Client;
  public drawingReceived$ = new Subject<any>();
  private chatSubject = new Subject<any>()
  public chatReceived$ = this.chatSubject.asObservable();

  constructor(private store: Store) {
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS('http://localhost:9090/ws'),
      reconnectDelay: 5000,
      debug: str => console.log('[STOMP]', str)
    });
  }

  public connect(roomId: string): void {
    this.stompClient.onConnect = () => {
      console.log('Connected to WebSocket');

      this.stompClient.subscribe(`/topic/game/${roomId}/state`, (message: IMessage) => {
        const session: GameSession = JSON.parse(message.body);
        this.store.dispatch(GameActions.updateFromSocket({ session }));
      });

      this.stompClient.subscribe(`/topic/game/${roomId}/chat`, msg => {
        const chatMsg = JSON.parse(msg.body);
        this.chatSubject = JSON.parse(msg.body);
        console.log('Chat:', chatMsg);
      });

      this.stompClient.subscribe(`/topic/game/${roomId}/draw`, msg => {
        const drawingData = JSON.parse(msg.body);
        console.log('Drawing data:', drawingData);
      });

      this.stompClient.subscribe('/user/queue/notifications', msg => {
        const notif = JSON.parse(msg.body);
        console.log('Notification:', notif);
      });
    };

    this.stompClient.activate();
  }

  public disconnect(): void {
    this.stompClient.deactivate();
  }

  public sendGuess(roomId: string, guess: string): void {
    this.stompClient.publish({
      destination: `/app/game/${roomId}/guess`,
      body: guess
    });
  }

  public sendDrawing(roomId: string, drawingData: any): void {
    this.stompClient.publish({
      destination: `/app/game/${roomId}/draw`,
      body: JSON.stringify(drawingData)
    });
  }
}
