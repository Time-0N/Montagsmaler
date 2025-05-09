import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { GameService } from '../../api/services/game.service';
import * as GameActions from './game-store.actions';
import { catchError, map, mergeMap, of } from 'rxjs';
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
