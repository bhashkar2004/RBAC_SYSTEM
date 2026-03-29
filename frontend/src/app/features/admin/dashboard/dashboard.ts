import { Component, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent {
  private http = inject(HttpClient);
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);

  stats = {
    users: 0,
    roles: 0,
    permissions: 0,
    logs: 0
  };

  recentLogs: any[] = [];
  pendingRequests: any[] = [];

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadDashboard();
    }
  }

  loadDashboard() {
    forkJoin({
      users: this.http.get<any[]>('http://127.0.0.1:8085/api/user').pipe(catchError(() => of([]))),
      roles: this.http.get<any[]>('http://127.0.0.1:8085/api/roles').pipe(catchError(() => of([]))),
      permissions: this.http.get<any[]>('http://127.0.0.1:8085/api/permissions').pipe(catchError(() => of([]))),
      logs: this.http.get<any[]>('http://127.0.0.1:8085/api/logs').pipe(catchError(() => of([]))),
      requests: this.http.get<any[]>('http://127.0.0.1:8085/api/requests/pending').pipe(catchError(() => of([])))
    }).subscribe(data => {
      this.stats.users = data.users.length;
      this.stats.roles = data.roles.length;
      this.stats.permissions = data.permissions.length;
      this.stats.logs = data.logs.length;
      this.recentLogs = data.logs.slice(0, 5);
      this.pendingRequests = data.requests;
      this.cdr.detectChanges();
    });
  }

  approveRequest(id: number) {
    this.http.put(`http://127.0.0.1:8085/api/requests/${id}/approve`, {}).subscribe(() => {
      this.pendingRequests = this.pendingRequests.filter(r => r.id !== id);
      alert('Access Request Approved');
    });
  }

  rejectRequest(id: number) {
    this.http.put(`http://127.0.0.1:8085/api/requests/${id}/reject`, {}).subscribe(() => {
      this.pendingRequests = this.pendingRequests.filter(r => r.id !== id);
      alert('Access Request Denied');
    });
  }
}
