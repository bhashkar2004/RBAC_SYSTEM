import { Component, inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule, isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-partner-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="partner-page">
      <div class="glass-card partner-header">
        <h1>Partner Access Portal</h1>
        <p>Your limited access resources are listed below.</p>
      </div>
      
      <div class="resources-list">
        <div class="glass-card res-card" *ngFor="let r of resources">
          <h3>{{ r.resourceName }}</h3>
          <span class="type-badge">{{ r.resourceType }}</span>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .partner-page { display: flex; flex-direction: column; gap: 20px; }
    .partner-header { padding: 30px; }
    .resources-list { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; }
    .res-card { padding: 20px; text-align: center; }
    .type-badge { font-size: 0.7rem; color: var(--primary-glow); }
  `]
})
export class PartnerDashboardComponent {
  private http = inject(HttpClient);
  private platformId = inject(PLATFORM_ID);
  resources: any[] = [];

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.http.get<any[]>('http://127.0.0.1:8085/api/resources')
        .subscribe(res => this.resources = res);
    }
  }
}