import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { StorageService } from '../../../core/services/storage.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);
  private storage = inject(StorageService);

  username: string = '';
  password: string = '';

  ngOnInit() {
    // Ensure we are fully logged out to hide the app sidebar when on login page
    this.storage.removeItem('token');
    this.storage.removeItem('role');
    localStorage.removeItem('token');
    localStorage.removeItem('role');
  }

  login() {
    this.authService.login(this.username, this.password)
      .subscribe({
        next: (response) => {
          const token = response.token;
          const role = response.role || 'EMPLOYEE';

          this.authService.storeToken(token);
          this.storage.setItem('role', role);

          console.log("JWT Token:", token);
          
          if (role === 'ADMIN') this.router.navigate(['/admin']);
          else if (role === 'HR') this.router.navigate(['/hr']);
          else this.router.navigate(['/employee']);

        },

        error: () => {
          alert("Invalid username or password");
        }

      });

  }

}