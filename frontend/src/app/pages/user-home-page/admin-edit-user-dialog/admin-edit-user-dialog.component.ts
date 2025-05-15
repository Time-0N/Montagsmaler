import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import { UserWithId } from '../admin-user-list/admin-user-list.component';

@Component({
  selector: 'app-admin-edit-user-dialog',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDialogContent,
    MatDialogTitle,
    MatDialogActions
  ],
  templateUrl: './admin-edit-user-dialog.component.html',
  styleUrl: './admin-edit-user-dialog.component.scss'
})
export class AdminEditUserDialogComponent {
  editForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<AdminEditUserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { user: UserWithId }
  ) {
    this.editForm = this.fb.group({
      username: [data.user.user.username],
      email: [data.user.user.email],
      firstName: [data.user.user.firstName],
      lastName: [data.user.user.lastName],
      aboutMe: [data.user.user.aboutMe]
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (this.editForm.valid) {
      this.dialogRef.close(this.editForm.value);
    }
  }
}
