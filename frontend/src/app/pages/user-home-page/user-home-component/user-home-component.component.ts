import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';

import { User } from '../../../generated/model/user';
import { UserUpdateRequest } from '../../../generated/model/userUpdateRequest';
import { updateUser, deleteUser } from '../../../store/user-store/user-store.actions';
import { selectUser, selectUserLoading } from '../../../store/user-store/user-store.selectors';
import { AuthWrapperService } from '../../../core/auth/auth-wrapper.service';
import { UserEditFormComponent } from '../user-edit-form/user-edit-form.component';
import { LogoutConfirmDialogComponent } from '../logout-confirm-dialog/logout-confirm-dialog.component';
import { DeleteConfirmDialogComponent } from '../delete-confirm-dialog/delete-confirm-dialog.component';
import { CreateGameButtonComponent } from '../create-game-button/create-game-button.component';
import { JoinGameFormComponent } from '../join-game-form/join-game-form.component';
import { AdminUserListComponent } from '../admin-user-list/admin-user-list.component';
import { MatCardModule } from '@angular/material/card';
import { NgIf, AsyncPipe } from '@angular/common';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-user-home-component',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe,
    MatCardModule,
    UserEditFormComponent,
    CreateGameButtonComponent,
    JoinGameFormComponent,
    AdminUserListComponent,
    MatDialogModule,
    MatButton
  ],
  templateUrl: './user-home-component.component.html',
  styleUrls: ['./user-home-component.component.scss']
})
export class UserHomeComponentComponent implements OnInit {
  user$!: Observable<User | null>;
  loading$!: Observable<boolean>;
  editMode = false;
  public isAdmin = false;

  constructor(
    private store: Store,
    private router: Router,
    private authWrapper: AuthWrapperService,
    private dialog: MatDialog
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

  openLogoutDialog(): void {
    const dialogRef = this.dialog.open(LogoutConfirmDialogComponent, {
      width: '350px',
      backdropClass: 'custom-dialog-backdrop',
      panelClass: 'custom-dialog-panel'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'confirmed') {
        this.onLogoutConfirmed();
      }
    });
  }

  openDeleteDialog(): void {
    const dialogRef = this.dialog.open(DeleteConfirmDialogComponent, {
      data: {
        message: 'Are you sure you want to delete your account?',
        confirmationWord: 'confirm'
      },
      width: '350px',
      backdropClass: 'custom-dialog-backdrop',
      panelClass: 'custom-dialog-panel'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'confirmed') {
        this.onDeleteConfirmed();
      }
    });
  }

  onLogoutConfirmed(): void {
    this.authWrapper.logout();
    this.router.navigate(['/']);
  }

  onDeleteConfirmed(): void {
    this.store.dispatch(deleteUser());
  }
}
