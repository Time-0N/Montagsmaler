import { Component, OnDestroy, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AsyncPipe, NgIf } from '@angular/common';
import { Store } from '@ngrx/store';
import { Observable, Subject, filter, takeUntil, take } from 'rxjs';
import { Router } from '@angular/router';

import { User } from '../../../generated/model/user';
import { UserUpdateRequest } from '../../../generated/model/userUpdateRequest';
import { updateUser, deleteUser } from '../../../store/user-store/user-store.actions';
import { selectUser, selectUserLoading } from '../../../store/user-store/user-store.selectors';
import { AuthWrapperService } from '../../../core/auth/auth-wrapper.service';
import {UserEditFormComponent} from '../user-edit-form/user-edit-form.component';
import {LogoutConfirmDialogComponent} from '../logout-confirm-dialog/logout-confirm-dialog.component';
import {DeleteConfirmDialogComponent} from '../delete-confirm-dialog/delete-confirm-dialog.component';
import {CreateGameButtonComponent} from '../create-game-button/create-game-button.component';
import {JoinGameFormComponent} from '../join-game-form/join-game-form.component';
import {AdminUserListComponent} from '../admin-user-list/admin-user-list.component';


@Component({
  selector: 'app-user-home-component',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, AsyncPipe, UserEditFormComponent, LogoutConfirmDialogComponent, DeleteConfirmDialogComponent, CreateGameButtonComponent, JoinGameFormComponent, AdminUserListComponent],
  templateUrl: './user-home-component.component.html',
  styleUrls: ['./user-home-component.component.scss']
})
export class UserHomeComponentComponent implements OnInit {
  user$!: Observable<User | null>;
  loading$!: Observable<boolean>;
  editMode = false;
  showLogoutDialog = false;
  showDeleteDialog = false;
  public isAdmin = false;

  constructor(
    private store: Store,
    private router: Router,
    private authWrapper: AuthWrapperService
  ) {
    this.user$ = this.store.select(selectUser);
    this.loading$ = this.store.select(selectUserLoading);
  }

  ngOnInit(): void {
    this.isAdmin = this.authWrapper.hasAnyRole(['ADMIN']);
  }

  toggleEdit(): void {
    this.editMode = !this.editMode;
  }

  handleUpdate(update: Partial<UserUpdateRequest>): void {
    this.store.dispatch(updateUser({ update }));
    this.editMode = false;
  }

  openLogoutDialog() {
    this.showLogoutDialog = true;
  }

  openDeleteDialog() {
    this.showDeleteDialog = true;
  }

  onLogoutConfirmed() {
    this.authWrapper.logout();
    this.router.navigate(['/'])
  }

  onDeleteConfirmed() {
    this.store.dispatch(deleteUser());
  }
}
