import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../../../api/services/auth.service';
import {Router} from '@angular/router';
import {AuthenticationRequest} from '../../../api/models/authenticationRequest';
import {UserRegistrationRequest} from '../../../api/models/userRegistrationRequest';
import {TokenResponse} from '../../../api/models/tokenResponse';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-home-page-component',
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './home-page-component.component.html',
  styleUrl: './home-page-component.component.scss'
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
    private router: Router
  ) {}

  ngOnInit(): void {
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
          this.errorMessage = err.error?.message || 'Login failed';
          this.isLoading = false;
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
          if (response.token) {
            this.handleAuthentication(response.token);
            this.router.navigate(['/dashboard']);
          }
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Registration failed';
          this.isLoading = false;
        }
      });
  }

  private handleAuthentication(tokenResponse: TokenResponse): void {
    localStorage.setItem('access_token', tokenResponse.access_token || '');
    localStorage.setItem('refresh_token', tokenResponse.refresh_token || '');
    this.isLoading = false;
  }
}
