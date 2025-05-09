import { createReducer, on } from '@ngrx/store';
import * as GameActions from './game-store.actions';
import { GameState } from './game-store.state';

export const gameInitialState: GameState = {
  currentSession: null,
  loading: false,
  error: null
};

export const gameReducer = createReducer(
  gameInitialState,

  on(GameActions.setGameSession, (state, { session }) => ({
    ...state,
    currentSession: session,
    error: null
  })),

  on(GameActions.updateFromSocket, (state, { session }) => ({
    ...state,
    currentSession: session
  })),

  on(GameActions.createGame, GameActions.joinGame, GameActions.toggleReady, GameActions.submitWord, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(GameActions.createGameSuccess, GameActions.joinGameSuccess, (state, { session }) => ({
    ...state,
    currentSession: session,
    loading: false,
    error: null
  })),

  on(GameActions.createGameFailure, GameActions.joinGameFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);
