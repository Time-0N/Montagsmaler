import { createReducer, on } from '@ngrx/store';
import * as UserActions from './user-store.actions';
import {UserState} from './user-store.state';

export const userInitialState: UserState = {
  user: null,
  loading: true,
  loaded: false,
  error: null
}

export const userFeatureName = 'userData';

export const userReducer = createReducer(
  userInitialState,

  on(UserActions.loadUserDataSuccess, (state, action): UserState => {
    return {
      ...state,
      user: action.user,
      loading: false,
      loaded: true,
      error: null
    };
  }),

  on(UserActions.loadUserDataFailure, (state, { error }) => ({
    ...state,
    error,
    loading: false
  })),

  on(UserActions.updateUserSuccess, (state, { user }) => ({
    ...state,
    user
  }))
);
