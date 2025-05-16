import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpEvent } from '@angular/common/http';
import { of, throwError } from 'rxjs';

import { HomePageComponentComponent } from './home-page-component.component';
import { AuthService } from '../../../generated';
import { AuthWrapperService } from '../../../core/auth/auth-wrapper.service';
import { Router } from '@angular/router';
import { TokenResponse } from '../../../generated';
import { UserRegistrationRequest } from '../../../generated';

describe('HomePageComponentComponent', () => {
  let component: HomePageComponentComponent;
  let fixture: ComponentFixture<HomePageComponentComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let authWrapperSpy: jasmine.SpyObj<AuthWrapperService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['loginUser', 'registerUser']);
    authWrapperSpy = jasmine.createSpyObj('AuthWrapperService', ['storeToken']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        HomePageComponentComponent,
        ReactiveFormsModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: AuthWrapperService, useValue: authWrapperSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HomePageComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize login and register forms on init', () => {
    expect(component.loginForm).toBeDefined();
    expect(component.registerForm).toBeDefined();
    expect(component.loginForm.controls['username']).toBeDefined();
    expect(component.registerForm.controls['email']).toBeDefined();
  });

  it('should toggle auth mode and reset error', () => {
    component.errorMessage = 'some error';
    component.isLoginMode = true;
    component.toggleAuthMode();
    expect(component.isLoginMode).toBeFalse();
    expect(component.errorMessage).toBe('');
    component.toggleAuthMode();
    expect(component.isLoginMode).toBeTrue();
  });

  it('should return correct form via getter', () => {
    component.isLoginMode = true;
    expect(component.form).toBe(component.loginForm);
    component.isLoginMode = false;
    expect(component.form).toBe(component.registerForm);
  });

  describe('onLogin', () => {
    const creds = { username: 'user', password: 'pass' };

    it('should not call loginUser if form invalid', () => {
      component.loginForm.setValue({ username: '', password: '' });
      component.onLogin();
      expect(authServiceSpy.loginUser).not.toHaveBeenCalled();
    });

    it('should login and navigate on success', () => {
      const token: TokenResponse = { accessToken: 'abc' };
      authServiceSpy.loginUser.and.returnValue(
        of(token) as unknown as import('rxjs').Observable<HttpEvent<TokenResponse>>
      );
      component.loginForm.setValue(creds);

      component.onLogin();
      expect(authWrapperSpy.storeToken).toHaveBeenCalledWith(token);
      expect(routerSpy.navigate).toHaveBeenCalledWith(['/user-home']);
      expect(component.isLoading).toBeFalse();
    });

    it('should handle error on login failure', fakeAsync(() => {
      const errorResponse = { error: { message: 'Bad creds' } };
      authServiceSpy.loginUser.and.returnValue(throwError(() => errorResponse));
      component.loginForm.setValue(creds);

      component.onLogin();
      tick();

      expect(component.errorMessage).toBe('Bad creds');
      expect(component.isLoading).toBeFalse();
    }));
  });

  describe('onRegister', () => {
    const reg: UserRegistrationRequest = {
      username: 'user',
      email: 'a@b.com',
      password: 'pass',
      firstName: 'First',
      lastName: 'Last'
    };

    beforeEach(() => {
      component.toggleAuthMode();
    });

    it('should not call registerUser if form invalid', () => {
      component.registerForm.setValue({ username: '', email: '', password: '', firstName: '', lastName: '' });
      component.onRegister();
      expect(authServiceSpy.registerUser).not.toHaveBeenCalled();
    });

    it('should register and navigate on success', () => {
      const token: TokenResponse = { accessToken: 'xyz' };
      authServiceSpy.registerUser.and.returnValue(
        of({ token } as any) as unknown as import('rxjs').Observable<HttpEvent<{ token: TokenResponse }>>
      );
      component.registerForm.setValue(reg);

      component.onRegister();
      expect(authWrapperSpy.storeToken).toHaveBeenCalledWith(token);
      expect(routerSpy.navigate).toHaveBeenCalledWith(['/user-home']);
      expect(component.isLoading).toBeFalse();
    });

    it('should handle error on registration failure', fakeAsync(() => {
      const errorResponse = { error: { error: { message: 'Registration error' } } };
      authServiceSpy.registerUser.and.returnValue(throwError(() => errorResponse));
      component.registerForm.setValue(reg);

      component.onRegister();
      tick();

      expect(component.errorMessage).toBe('Registration error');
      expect(component.isLoading).toBeFalse();
    }));
  });
});
