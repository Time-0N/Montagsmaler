import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthWrapperService } from './services/auth-wrapper.service';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthWrapperService);
  const router = inject(Router);

  if (!auth.isAuthenticated()) {
    router.navigate(['/']);
    return false;
  }
  return true;
};
