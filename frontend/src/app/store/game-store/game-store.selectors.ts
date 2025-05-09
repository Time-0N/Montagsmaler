import { createFeatureSelector, createSelector } from '@ngrx/store';
import { GameState } from './game-store.state';

export const gameFeatureKey = 'game';

export const selectGameState = createFeatureSelector<GameState>(gameFeatureKey);

export const selectCurrentSession = createSelector(
  selectGameState,
  (state) => state.currentSession
);

export const selectGameLoading = createSelector(
  selectGameState,
  (state) => state.loading
);

export const selectGameError = createSelector(
  selectGameState,
  (state) => state.error
);

export const selectGamePhase = createSelector(
  selectCurrentSession,
  (session) => session?.phase
);

export const selectCurrentDrawer = createSelector(
  selectCurrentSession,
  (session) => session?.currentDrawer
);

export const selectTimerSeconds = createSelector(
  selectCurrentSession,
  (session) => session?.timerSeconds
);

export const selectGameSession = createSelector(
  selectGameState,
  (state) => state.currentSession
);
