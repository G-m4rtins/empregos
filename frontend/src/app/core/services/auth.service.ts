import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, firstValueFrom, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, RegisterRequest, TokenResponse, User } from '../models/user.model';

const ACCESS_TOKEN_KEY = 'empregos_access_token';
const REFRESH_TOKEN_KEY = 'empregos_refresh_token';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);

  private readonly currentUserSignal = signal<User | null>(null);
  readonly currentUser = this.currentUserSignal.asReadonly();
  readonly isAuthenticated = computed(() => this.currentUserSignal() !== null);
  readonly isCompany = computed(() => this.currentUserSignal()?.role === 'COMPANY');
  readonly isCandidate = computed(() => this.currentUserSignal()?.role === 'CANDIDATE');

  private readonly initialized: Promise<void>;

  constructor() {
    this.initialized = this.getAccessToken()
      ? firstValueFrom(this.fetchCurrentUser()).then(
          () => undefined,
          () => this.clearSession(),
        )
      : Promise.resolve();
  }

  whenReady(): Promise<void> {
    return this.initialized;
  }

  login(payload: LoginRequest): Observable<User> {
    return new Observable<User>((subscriber) => {
      this.http.post<TokenResponse>(`${environment.apiUrl}/auth/login`, payload).subscribe({
        next: (tokens) => {
          this.storeTokens(tokens);
          this.fetchCurrentUser().subscribe({
            next: (user) => {
              subscriber.next(user);
              subscriber.complete();
            },
            error: (err) => subscriber.error(err),
          });
        },
        error: (err) => subscriber.error(err),
      });
    });
  }

  register(payload: RegisterRequest): Observable<User> {
    return new Observable<User>((subscriber) => {
      this.http.post(`${environment.apiUrl}/auth/register`, payload).subscribe({
        next: () => {
          this.login({ email: payload.email, password: payload.password }).subscribe({
            next: (user) => {
              subscriber.next(user);
              subscriber.complete();
            },
            error: (err) => subscriber.error(err),
          });
        },
        error: (err) => subscriber.error(err),
      });
    });
  }

  refreshSession(): Observable<TokenResponse> {
    const refreshToken = this.getRefreshToken();
    return this.http
      .post<TokenResponse>(`${environment.apiUrl}/auth/refresh`, { refreshToken })
      .pipe(tap((tokens) => this.storeTokens(tokens)));
  }

  logout(): void {
    this.clearSession();
  }

  getAccessToken(): string | null {
    return localStorage.getItem(ACCESS_TOKEN_KEY);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(REFRESH_TOKEN_KEY);
  }

  private fetchCurrentUser(): Observable<User> {
    return this.http
      .get<User>(`${environment.apiUrl}/auth/me`)
      .pipe(tap((user) => this.currentUserSignal.set(user)));
  }

  private storeTokens(tokens: TokenResponse): void {
    localStorage.setItem(ACCESS_TOKEN_KEY, tokens.accessToken);
    localStorage.setItem(REFRESH_TOKEN_KEY, tokens.refreshToken);
  }

  private clearSession(): void {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    this.currentUserSignal.set(null);
  }
}
