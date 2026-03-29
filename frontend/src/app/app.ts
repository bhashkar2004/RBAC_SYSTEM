import { Component, PLATFORM_ID, inject } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  router = inject(Router);
  private platformId = inject(PLATFORM_ID);
  private http = inject(HttpClient);

  showProfileModal = false;
  myProfile: any = null;

  openProfileModal() {
    this.showProfileModal = true;
    if (isPlatformBrowser(this.platformId)) {
      this.http.get<any>('http://127.0.0.1:8085/api/user/me')
        .subscribe({
          next: (res) => this.myProfile = res,
          error: (err) => console.error("Could not fetch profile", err)
        });
    }
  }

  closeProfileModal() {
    this.showProfileModal = false;
  }

  isLoggedIn() {
    if (isPlatformBrowser(this.platformId)) {
      return !!localStorage.getItem('token');
    }
    return false;
  }

  getRole() {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('role') || 'EMPLOYEE';
    }
    return 'EMPLOYEE';
  }

  logout() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token');
      localStorage.removeItem('role');
    }
    this.router.navigate(['/login']);
  }
}
