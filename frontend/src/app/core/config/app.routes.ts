import { Routes } from '@angular/router';
import { HomePageComponent } from '../../pages/home-page/home-page/home-page.component';
import { UserHomeComponent } from '../../pages/user-home-page/user-home/user-home.component';
import {authGuard} from '../auth/auth.guard';
import {resolveUserDataConfiguration} from '../../resolvers/user.resolver'

export const routes: Routes = [
  {
    path: '',
    component: HomePageComponent
  },
  {
    path: 'user-home',
    component: UserHomeComponent,
    resolve: { userDataResolver: resolveUserDataConfiguration },
    canActivate: [authGuard]
  },
  {
    path: 'game/:roomId',
    loadComponent: () => import('../../pages/game-page/game-page/game-page.component').then(m => m.GamePageComponent),
    canActivate: [authGuard]
  },
  {
    path: '**', redirectTo: ''
  }
];
