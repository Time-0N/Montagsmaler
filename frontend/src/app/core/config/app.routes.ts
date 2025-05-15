import { Routes } from '@angular/router';
import { HomePageComponentComponent } from '../../pages/home-page/home-page-component/home-page-component.component';
import { UserHomeComponentComponent } from '../../pages/user-home-page/user-home-component/user-home-component.component';
import {authGuard} from '../auth/auth.guard';
import {resolveUserDataConfiguration} from '../../resolvers/user.resolver'

export const routes: Routes = [
  {
    path: '',
    component: HomePageComponentComponent
  },
  {
    path: 'user-home',
    component: UserHomeComponentComponent,
    resolve: { userDataResolver: resolveUserDataConfiguration },
    canActivate: [authGuard]
  },
  {
    path: 'game/:roomId',
    loadComponent: () => import('../../pages/game-page/game-page/game-page.component').then(m => m.GamePageComponent),
  },
  {
    path: '**', redirectTo: ''
  }
];
