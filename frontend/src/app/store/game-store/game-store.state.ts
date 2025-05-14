import {GameSession} from '../../api/model/gameSession';

export interface GameState {
  currentSession: GameSession | null;
  loading: boolean,
  error: string | null
}

export const initialGameState: GameState = {
  currentSession: null,
  loading: false,
  error: null
};
