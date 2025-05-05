import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../api/services/auth.service';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { AuthWrapperService } from '../../../auth/services/auth-wrapper.service';
import { AuthenticationRequest } from '../../../api/models/authenticationRequest';
import { UserRegistrationRequest } from '../../../api/models/userRegistrationRequest';
import { TokenResponse } from '../../../api/models/tokenResponse';


@Component({
  selector: 'app-home-page-component',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './home-page-component.component.html',
  styleUrls: ['./home-page-component.component.scss']
})
export class HomePageComponentComponent implements OnInit {
  isLoginMode = true;
  loginForm!: FormGroup;
  registerForm!: FormGroup;
  errorMessage = '';
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private authWrapper: AuthWrapperService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initializeForms();
  }

  private initializeForms(): void {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      firstName: [''],
      lastName: ['']
    });
  }

  toggleAuthMode(): void {
    this.isLoginMode = !this.isLoginMode;
    this.errorMessage = '';
  }

  onLogin(): void {
    if (this.loginForm.invalid) return;

    this.isLoading = true;
    this.errorMessage = '';

    const authRequest: AuthenticationRequest = {
      username: this.loginForm.value.username,
      password: this.loginForm.value.password
    };

    this.authService.loginUser({ authenticationRequest: authRequest })
      .subscribe({
        next: (response) => {
          this.handleAuthentication(response);
          this.router.navigate(['/user-home']);
        },
        error: (err) => {
          this.handleError(err, 'Login failed');
        }
      });
  }

  onRegister(): void {
    if (this.registerForm.invalid) return;

    this.isLoading = true;
    this.errorMessage = '';

    const registrationRequest: UserRegistrationRequest = {
      username: this.registerForm.value.username,
      email: this.registerForm.value.email,
      password: this.registerForm.value.password,
      firstName: this.registerForm.value.firstName,
      lastName: this.registerForm.value.lastName
    };

    this.authService.registerUser({ userRegistrationRequest: registrationRequest })
      .subscribe({
        next: (response) => {
          if (response.token?.access_token) {
            this.handleAuthentication(response.token);
            this.router.navigate(['/user-home']);
          }
        },
        error: (err) => {
          this.handleError(err, 'Registration failed');
        }
      });
  }

  private handleAuthentication(tokenResponse: TokenResponse): void {
    if (!tokenResponse?.access_token) {
      this.errorMessage = 'Invalid authentication response';
      this.isLoading = false;
      return;
    }
    this.authWrapper.storeToken(tokenResponse);
    this.isLoading = false;
  }

  private handleError(err: any, defaultMessage: string): void {
    this.errorMessage = err.error?.error?.message ||
      err.error?.message ||
      defaultMessage;
    this.isLoading = false;
  }
}
