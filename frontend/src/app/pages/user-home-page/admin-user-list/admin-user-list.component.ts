import { Component, OnInit } from '@angular/core';
import {AdminService, User, UserService} from '../../../generated';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import {MatCard} from '@angular/material/card';
import {
  MatCell, MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import {MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {DeleteConfirmDialogComponent} from '../delete-confirm-dialog/delete-confirm-dialog.component';
import { AdminEditUserDialogComponent } from '../admin-edit-user-dialog/admin-edit-user-dialog.component';

export interface UserWithId {
  id: string;
  user: User;
}

@Component({
  selector: 'app-admin-user-list',
  templateUrl: './admin-user-list.component.html',
  imports: [
    MatCard,
    MatTable,
    MatHeaderCell,
    MatColumnDef,
    MatHeaderCellDef,
    MatCell,
    MatIcon,
    MatIconButton,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRowDef,
    MatRow,
    MatCellDef
  ],
  styleUrls: ['./admin-user-list.component.scss']
})
export class AdminUserListComponent implements OnInit {
  users: UserWithId[] = [];  // Changed to UserWithId array
  displayedColumns: string[] = ['username', 'email', 'actions'];

  constructor(
    private adminService: AdminService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  private loadUsers(): void {
    this.adminService.getAllUsers().subscribe({
      next: (response) => {
        this.users = response.flatMap(userMap =>
          Object.entries(userMap).map(([id, user]) => ({ id, user }))
        );
      },
      error: (err) => this.showError('Failed to load users')
    });
  }

  deleteUser(userId: string): void {
    const dialogRef = this.dialog.open(DeleteConfirmDialogComponent, {
      data: {
        message: 'Are you sure you want to delete this user?',
        confirmationWord: 'confirm'
      },
      width: '350px',
      backdropClass: 'custom-dialog-backdrop',
      panelClass: 'custom-dialog-panel'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'confirmed') {
        this.adminService.adminDeleteUserById(userId).subscribe({
          next: () => {
            this.users = this.users.filter(u => u.id !== userId);
            this.snackBar.open('User deleted successfully', 'Close', { duration: 3000 });
          },
          error: () => this.showError('Failed to delete user')
        });
      }
    });
  }

  editUser(userWithId: UserWithId): void {
    const dialogRef = this.dialog.open(AdminEditUserDialogComponent, {
      width: '600px',
      data: { user: userWithId }
    });

    dialogRef.afterClosed().subscribe(updatedData => {
      if (updatedData) {
        this.adminService.adminUpdateUser(userWithId.id, updatedData).subscribe({
          next: () => {
            // Update local data
            const index = this.users.findIndex(u => u.id === userWithId.id);
            if (index > -1) {
              this.users[index] = {
                ...userWithId,
                user: { ...userWithId.user, ...updatedData }
              };
              this.users = [...this.users]; // Trigger change detection
              this.snackBar.open('User updated successfully', 'Close', { duration: 3000 });
            }
          },
          error: () => this.showError('Failed to update user')
        });
      }
    });
  }

  private showError(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }
}
