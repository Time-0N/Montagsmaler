import { createReducer, on } from '@ngrx/store';
import * as UserActions from './user-store.actions';
import { UserState } from './user-store.state';

export const userFeatureName = 'userData';

export const userInitialState: UserState = {
  user: null,
  loading: true,
  loaded: false,
  error: null
};

export const userReducer = createReducer(
  userInitialState,

  on(UserActions.loadUserDataSuccess, (state, { user }): UserState => ({
    ...state,
    user,
    loading: false,
    loaded: true,
    error: null
  })),

  on(UserActions.loadUserDataFailure, (state, { error }): UserState => ({
    ...state,
    error,
    loading: false
  })),

  on(UserActions.updateUser, (state): UserState => ({
    ...state,
    loading: true,
    error: null
  })),

  on(UserActions.updateUserSuccess, (state, { user }): UserState => ({
    ...state,
    user,
    loading: false
  })),

  on(UserActions.updateUserFailure, (state, { error }): UserState => ({
    ...state,
    error,
    loading: false
  }))
);
