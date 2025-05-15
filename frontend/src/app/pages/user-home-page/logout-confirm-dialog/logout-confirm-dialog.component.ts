import {Component, EventEmitter, Output} from '@angular/core';
import {MatDialogModule} from '@angular/material/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import {MatButtonModule} from '@angular/material/button';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-logout-confirm-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule
  ],
  templateUrl: './logout-confirm-dialog.component.html',
  styleUrls: ['./logout-confirm-dialog.component.scss']
})
export class LogoutConfirmDialogComponent {
  constructor(private dialogRef: MatDialogRef<LogoutConfirmDialogComponent>) {}

  confirm(): void {
    this.dialogRef.close('confirmed');
  }

  cancel(): void {
    this.dialogRef.close('cancelled');
  }
}
