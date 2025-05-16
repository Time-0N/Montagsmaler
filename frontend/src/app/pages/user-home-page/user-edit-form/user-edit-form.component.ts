import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { User } from '../../../generated';
import { UserUpdateRequest } from '../../../generated';
import { CommonModule } from '@angular/common';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-user-edit-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './user-edit-form.component.html',
  styleUrls: ['./user-edit-form.component.scss']
})
export class UserEditFormComponent implements OnInit {
  @Input() user: User | null = null;
  @Output() submitForm = new EventEmitter<UserUpdateRequest>();
  @Output() cancelEdit = new EventEmitter<void>();

  profileForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      firstName: [''],
      lastName: [''],
      aboutMe: ['']
    });

    if (this.user) {
      this.profileForm.patchValue(this.user);
    }
  }

  onSave(): void {
    if (this.profileForm.valid) {
      this.submitForm.emit(this.profileForm.value);
    }
  }

  onCancel(): void {
    this.cancelEdit.emit();
  }
}
