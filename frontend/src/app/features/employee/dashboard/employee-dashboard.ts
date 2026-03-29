import { Component, inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-employee-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './employee-dashboard.html',
  styleUrl: './employee-dashboard.css'
})
export class EmployeeDashboardComponent {
  private http = inject(HttpClient);
  private platformId = inject(PLATFORM_ID);
  
  myProfile: any = null;
  myPermissions: any[] = [];
  request = { resource: '', reason: '' };
  showProfileModal = false;

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.http.get<any>('http://127.0.0.1:8085/api/user/me')
        .subscribe({
          next: (res) => this.myProfile = res,
          error: (err) => console.error("Could not fetch profile", err)
        });

      this.http.get<any[]>('http://127.0.0.1:8085/api/user/me/permissions')
        .subscribe(res => {
          this.myPermissions = res;
          
          this.http.get<any[]>('http://127.0.0.1:8085/api/requests/me')
            .subscribe(reqs => {
              const approvedRequests = reqs.map((r: any) => ({
                action: 'GRANTED',
                resource: r.resource
              }));
              this.myPermissions = [...this.myPermissions, ...approvedRequests];
            });
        });
    }
  }

  openProfile() {
    this.showProfileModal = true;
  }

  closeProfile() {
    this.showProfileModal = false;
  }

  requestAccess() {
    if (!this.request.resource || !this.request.reason) {
      alert("Please fill in both fields");
      return;
    }

    this.http.post('http://127.0.0.1:8085/api/requests', this.request)
      .subscribe({
        next: () => {
          alert(`Access request for ${this.request.resource} submitted successfully!`);
          this.request = { resource: '', reason: '' };
        },
        error: (err) => {
          console.error("Failed to submit request", err);
          alert("Failed to submit request.");
        }
      });
  }
}