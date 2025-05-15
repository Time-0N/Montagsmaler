import {Component, OnInit} from '@angular/core';
import {User, UserService} from '../../../generated';
import {MatCard} from '@angular/material/card';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef, MatHeaderRow,
  MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';

@Component({
  selector: 'app-admin-user-list',
  imports: [
    MatCard,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRowDef,
    MatHeaderRow,
    MatRow,
    MatRowDef
  ],
  templateUrl: './admin-user-list.component.html',
  styleUrl: './admin-user-list.component.scss'
})
export class AdminUserListComponent implements OnInit {
  users: User[] = [];
  displayedColumns: string[] = ['username', 'email']

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getAllUsers().subscribe(users => {
      this.users = users;
    })
  }
}
