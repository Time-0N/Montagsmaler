import { User } from '../../generated/model/user';

export interface UserState {
  readonly user: User | null,
  loading: boolean,
  loaded: boolean,
  error: string | null
}
