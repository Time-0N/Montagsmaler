import { User } from '../../api/models/user';

export interface UserState {
  readonly user: User | null,
  loading: boolean,
  loaded: boolean,
  error: string | null
}
