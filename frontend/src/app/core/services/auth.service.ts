import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { StorageService } from './storage.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private storage = inject(StorageService);

  private apiUrl = 'http://127.0.0.1:8085/auth/login';

  login(username: string, password: string) {
    return this.http.post<{ token: string, role?: string }>(this.apiUrl, { username, password });
  }

  storeToken(token: string) {
    this.storage.setItem('token', token);
  }

  getToken() {
    return this.storage.getItem('token');
  }

  logout() {
    this.storage.removeItem('token');
    this.storage.removeItem('role');
  }
}
