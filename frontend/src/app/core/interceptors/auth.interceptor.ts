import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthWrapperService } from '../../auth/services/auth-wrapper.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthWrapperService);
  const token = auth.getAccessToken();

  const publicEndpoints = ['/api/auth/token', '/api/auth/register'];

  const isPublic = publicEndpoints.some(url => req.url.includes(url));

  if (token && !isPublic) {
    return next(req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    }));
  }

  return next(req);
};
