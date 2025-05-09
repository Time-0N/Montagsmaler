import { createAction, props } from '@ngrx/store';
import { GameSession } from '../../api/models/gameSession'

const actionName = '[Game]';

export const setGameSession = createAction(
  `${actionName} Set Session`,
  props<{ session: GameSession }>()
);

export const createGame = createAction(`${actionName} Create`);
export const createGameSuccess = createAction(
  `${actionName} Create Success`,
  props<{ session: GameSession }>()
);
export const createGameFailure = createAction(
  `${actionName} Create Failure`,
  props<{ error: string }>()
);

export const joinGame = createAction(
  `${actionName} Join`,
  props<{ roomId: string }>()
);
export const joinGameSuccess = createAction(
  `${actionName} Join Success`,
  props<{ session: GameSession }>()
);
export const joinGameFailure = createAction(
  `${actionName} Join Failure`,
  props<{ error: string }>()
);

export const toggleReady = createAction(
  `${actionName} Toggle Ready`,
  props<{ roomId: string }>()
);
export const toggleReadySuccess = createAction(
  `${actionName} Toggle Ready Success`);
export const toggleReadyFailure = createAction(
  `${actionName} Toggle Ready Failure`,
  props<{ error: string }>());


export const submitWord = createAction(
  `${actionName} Submit Word`,
  props<{ word: string; roomId: string }>()
);
export const submitWordSuccess = createAction(
  `${actionName} Submit Word Success`);
export const submitWordFailure = createAction(
  `${actionName} Submit Word Failure`,
  props<{ error: string }>()
);

export const updateFromSocket = createAction(
  `${actionName} Update From Socket`,
  props<{ session: GameSession }>()
);
