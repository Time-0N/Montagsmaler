import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';
import { selectCurrentSession } from '../../../store/game-store/game-store.selectors';
import { toggleReady } from '../../../store/game-store/game-store.actions';
import { User } from '../../../generated';
import { UserService } from '../../../generated';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import {LeaveGameButtonComponent} from '../leave-game-button/leave-game-button.component';

@Component({
  selector: 'app-game-lobby',
  standalone: true,
  imports: [CommonModule, LeaveGameButtonComponent],
  templateUrl: './game-lobby.component.html',
  styleUrls: ['./game-lobby.component.scss']
})
export class GameLobbyComponent implements OnInit {
  roomId: string = '';
  users$: Observable<User[]> = new Observable();
  readyStatus$: Observable<{ [key: string]: boolean }> = new Observable();
  currentUser: User | null = null;

  constructor(
    private store: Store,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.roomId = window.location.pathname.split('/').pop()!;

    this.users$ = this.store.select(selectCurrentSession).pipe(
      map(session => session?.users ?? [])
    );

    this.readyStatus$ = this.store.select(selectCurrentSession).pipe(
      map(session => session?.readyStatus ?? {})
    );

    this.userService.getCurrentUser().subscribe(user => {
      this.currentUser = user;
    });
  }


  toggleReady(): void {
    if (this.roomId) {
      this.store.dispatch(toggleReady({ roomId: this.roomId }));
    }
  }
}
