import {inject, Injectable} from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { UserService } from '../../generated/api/user.service';
import * as UserActions from './user-store.actions';
import { catchError, map, mergeMap, of } from 'rxjs';
import { Router } from '@angular/router';
import { AuthWrapperService } from '../../core/auth/auth-wrapper.service';
import {userDataResolverExecuted} from './user-store.actions';

@Injectable()
export class UserStoreEffects {

    actions$ = inject(Actions)
    userService = inject(UserService)
    authWrapper = inject(AuthWrapperService);
    router = inject(Router);

  loadUser$ = createEffect(() =>
    this.actions$.pipe(
      ofType(userDataResolverExecuted),
      mergeMap(() =>
        this.userService.getCurrentUser().pipe(
          map(user => UserActions.loadUserDataSuccess({ user })),
          catchError(error => of(UserActions.loadUserDataFailure({ error })))
        )
      )
    )
  );

  updateUser$ = createEffect(() =>
    this.actions$.pipe(
      ofType(UserActions.updateUser),
      mergeMap(({ update }) =>
        this.userService.updateUser(update).pipe(
          map(updatedUser => UserActions.updateUserSuccess({ user: updatedUser })),
          catchError(error => of(UserActions.updateUserFailure({ error })))
        )
      )
    )
  );

  deleteUser$ = createEffect(() =>
    this.actions$.pipe(
      ofType(UserActions.deleteUser),
      mergeMap(() =>
        this.userService.deleteUser().pipe(
          map(() => {
            this.authWrapper.logout();
            this.router.navigate(['/']);
            return UserActions.deleteUserSuccess();
          }),
          catchError(error => of(UserActions.deleteUserFailure({ error })))
        )
      )
    )
  );
}
