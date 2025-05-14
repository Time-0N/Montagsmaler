import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {authInterceptor} from './core/interceptors/auth.interceptor';
import {BASE_PATH} from './api';
import {provideState, provideStore} from '@ngrx/store';
import {provideEffects} from '@ngrx/effects';
import {UserStoreEffects} from './store/user-store/user-store.effects';
import {userFeatureName, userReducer} from './store/user-store/user-store.reducer';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import {GameStoreEffects} from './store/game-store/game-store.effects';
import {gameFeatureKey, gameReducer} from './store/game-store/game-store.reducer';

export const appConfig: ApplicationConfig = {
  providers: [
    { provide: BASE_PATH, useValue: '/api' },
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([authInterceptor])
    ),
    provideStore(),
    provideState(gameFeatureKey, gameReducer),
    provideState(userFeatureName, userReducer),
    provideEffects([UserStoreEffects, GameStoreEffects]),
    provideStoreDevtools({ maxAge: 25, logOnly: false })
  ]
};
