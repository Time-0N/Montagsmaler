import { createAction, props } from '@ngrx/store';
import { UserUpdateRequest } from '../../generated/model/userUpdateRequest';
import {User} from '../../generated/model/user';

const actionName = '[user]';

export const userDataResolverExecuted = createAction(`${actionName} Resolver Executed`);

export const loadUserDataSuccess = createAction(
  `${actionName} Load Success`,
  props<{ user: User }>()
);

export const loadUserDataFailure = createAction(
  `${actionName} Load Failure`,
  props<{ error: string }>()
);

export const updateUser = createAction(
  `${actionName} Update`,
  props<{ update: UserUpdateRequest }>()
);

export const updateUserSuccess = createAction(
  `${actionName} Update Success`,
  props<{ user: User }>()
);

export const updateUserFailure = createAction(
  `${actionName} Update Failure`,
  props<{ error: string }>()
);

export const deleteUser = createAction(`${actionName} Delete`);

export const deleteUserSuccess = createAction(`${actionName} Delete Success`);

export const deleteUserFailure = createAction(
  `${actionName} Delete Failure`,
  props<{ error: any }>()
);
