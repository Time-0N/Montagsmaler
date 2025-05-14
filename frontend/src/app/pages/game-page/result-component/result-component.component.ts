import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';
import { selectCurrentSession } from '../../../store/game-store/game-store.selectors';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { User } from '../../../api/model/user';

@Component({
  selector: 'app-result-component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './result-component.component.html',
  styleUrls: ['./result-component.component.scss']
})
export class ResultComponentComponent {
  submittedWords$: Observable<{ user: User; word: string }[]>;

  constructor(private store: Store) {
    this.submittedWords$ = this.store.select(selectCurrentSession).pipe(
      map(session => {
        const users = session?.users ?? [];
        const words = session?.submittedWords ?? {};
        return users.map((user: any) => ({
          user,
          word: words[user ?? ''] ?? 'N/A'
        }));
      })
    );
  }
}
