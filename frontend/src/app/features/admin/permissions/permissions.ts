import { Component, inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule, isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-permissions',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './permissions.html',
  styleUrl: './permissions.css'
})
export class PermissionsComponent {
  private http = inject(HttpClient);
  private platformId = inject(PLATFORM_ID);
  permissions: any[] = [];

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.http.get<any[]>('http://127.0.0.1:8085/api/permissions')
        .subscribe(res => this.permissions = res);
    }
  }
}
