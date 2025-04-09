import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Client, IMessage, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-dummy',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dummy.component.html',
  styleUrls: ['./dummy.component.scss']
})
export class DummyComponent implements OnInit {
  private stompClient: Client | null = null;
  token: string = '';
  roomId: string = '';
  messageLog: string[] = [];
  baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {}

  login() {
    const loginPayload = {
      username: 'testuser',
      password: 'password'
    };

    this.http.post(`${this.baseUrl}/api/auth/token`, loginPayload)
      .subscribe((res: any) => {
        this.token = res.access_token;
        console.log('Logged in, token:', this.token);
      });
  }

  createGame() {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}`);
    this.http.post(`${this.baseUrl}/api/game/create`, {}, { headers })
      .subscribe((res: any) => {
        this.roomId = res.roomId;
        console.log('Game created with roomId:', this.roomId);
        this.connectToWebSocket();
      });
  }

  sendGuess() {
    if (!this.stompClient || !this.stompClient.connected) return;

    const message = 'banana';
    this.stompClient.publish({
      destination: `/app/game/${this.roomId}/guess`,
      body: message
    });

    this.messageLog.push(`Sent guess: ${message}`);
  }

  connectToWebSocket() {
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(`${this.baseUrl}/ws`),
      connectHeaders: {
        Authorization: `Bearer ${this.token}`
      },
      debug: (msg) => console.log(msg),
      reconnectDelay: 5000,
    });

    this.stompClient.onConnect = () => {
      console.log('WebSocket connected');

      this.stompClient?.subscribe(
        `/topic/game/${this.roomId}/updates`,
        (message: IMessage) => {
          this.messageLog.push(`Received: ${message.body}`);
        }
      );
    };

    this.stompClient.activate();
  }
}
