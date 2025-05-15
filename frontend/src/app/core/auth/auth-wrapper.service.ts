import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../generated/api/auth.service';
import { TokenResponse } from '../../generated/model/tokenResponse';

@Injectable({ providedIn: 'root' })
export class AuthWrapperService {
  constructor(
    private openApiAuth: AuthService,
    private router: Router
  ) {}

  storeToken(response: TokenResponse): void {
    localStorage.setItem('access_token', response.accessToken ?? '');
    localStorage.setItem('refresh_token', response.refreshToken ?? '');
  }

  getAccessToken(): string | null {
    return localStorage.getItem('access_token');
  }

  isAuthenticated(): boolean {
    const token = this.getAccessToken();
    return !!token && !this.isTokenExpired(token);
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return Date.now() >= payload.exp * 1000;
    } catch {
      return true;
    }
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/']);
  }
}
