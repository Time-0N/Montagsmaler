import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../generated';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { AuthWrapperService } from '../../../core/auth/auth-wrapper.service';
import { AuthenticationRequest } from '../../../generated';
import { UserRegistrationRequest } from '../../../generated';
import { TokenResponse } from '../../../generated';
import { MatCardModule } from '@angular/material/card';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';



@Component({
  selector: 'app-home-page-component',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    MatCardModule,
    MatButtonToggleModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatButtonModule
  ],
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss']
})
export class HomePageComponent implements OnInit {
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

    this.authService.loginUser(authRequest)
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

    this.authService.registerUser(registrationRequest)
      .subscribe({
        next: (response) => {
          if (response.token?.accessToken) {
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
    if (!tokenResponse?.accessToken) {
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

  get form(): FormGroup {
    return this.isLoginMode ? this.loginForm : this.registerForm;
  }
}
