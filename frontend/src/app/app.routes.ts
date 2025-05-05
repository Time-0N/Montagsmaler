import { Routes } from '@angular/router';
import { HomePageComponentComponent } from './pages/home-page/home-page-component/home-page-component.component';
import { UserHomeComponentComponent } from './pages/user-home/user-home-component/user-home-component.component';
import {authGuard} from './auth/auth.guard';

export const routes: Routes = [
  {
  path: '',
  component: HomePageComponentComponent
  },
  {
    path: 'user-home',
    component: UserHomeComponentComponent,
    canActivate: [authGuard]
  },
  {
    path: '**', redirectTo: ''
  }
];
