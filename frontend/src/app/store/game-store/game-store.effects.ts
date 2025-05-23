import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { GameService } from '../../generated/api/game.service';
import * as GameActions from './game-store.actions';
import {catchError, map, mergeMap, of, tap} from 'rxjs';
import { Router } from '@angular/router';

@Injectable()
export class GameStoreEffects {
  private actions$ = inject(Actions);
  private gameService = inject(GameService);
  private router = inject(Router);

  createGame$ = createEffect(() =>
    this.actions$.pipe(
      ofType(GameActions.createGame),
      mergeMap(() =>
        this.gameService.createGame().pipe(
          tap(session => {
            console.log('[Effect] Game created:', session);
            this.router.navigate(['/game', session.roomId]);
          }),
          map(session => GameActions.createGameSuccess({ session })),
          catchError(error => of(GameActions.createGameFailure({ error })))
        )
      )
    )
  );

  joinGame$ = createEffect(() =>
    this.actions$.pipe(
      ofType(GameActions.joinGame),
      mergeMap(({ roomId }) =>
        this.gameService.joinGame(roomId).pipe(
          map((session) => GameActions.joinGameSuccess({ session })),
          catchError(error => of(GameActions.joinGameFailure({ error })))
        )
      )
    )
  );

  navigateOnJoinSuccess$ = createEffect(() =>
      this.actions$.pipe(
        ofType(GameActions.joinGameSuccess),
        tap(({ session }) => {
          console.log('[Effect] Navigating to joined game:', session.roomId);
          this.router.navigate(['/game', session.roomId]);
        })
      ),
    { dispatch: false }
  );

  toggleReady$ = createEffect(() =>
    this.actions$.pipe(
      ofType(GameActions.toggleReady),
      mergeMap(({ roomId }) =>
        this.gameService.toggleReady(roomId).pipe(
          map(() => GameActions.toggleReadySuccess()),
          catchError(error => of(GameActions.toggleReadyFailure({ error })))
        )
      )
    )
  );

  leaveGame$ = createEffect(() =>
    this.actions$.pipe(
      ofType(GameActions.leaveGame),
      mergeMap(({ roomId }) =>
        this.gameService.leaveGame(roomId).pipe(
          tap(() => {
            this.router.navigate(['/user-home'])
          }),
          map(() => GameActions.leaveGameSuccess()),
          catchError(error => of(GameActions.leaveGameFailure({ error })))
        )
      )
    )
  )

  submitWord$ = createEffect(() =>
    this.actions$.pipe(
      ofType(GameActions.submitWord),
      mergeMap(({ roomId, word }) =>
        this.gameService.submitWord(roomId, word).pipe(
          map(() => GameActions.submitWordSuccess()),
          catchError(error => of(GameActions.submitWordFailure({ error })))
        )
      )
    )
  );
}
