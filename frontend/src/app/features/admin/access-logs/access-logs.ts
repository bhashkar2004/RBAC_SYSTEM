import { Component, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-logs',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './access-logs.html',
  styleUrl: './access-logs.css'
})
export class LogsComponent {
  private http = inject(HttpClient);
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);
  
  logs: any[] = [];
  filteredLogs: any[] = [];
  searchTerm: string = '';

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.http.get<any[]>('http://127.0.0.1:8085/api/logs')
        .pipe(catchError(() => of([])))
        .subscribe(res => {
          this.logs = res;
          this.applyFilter();
        });
    }
  }

  applyFilter() {
    this.filteredLogs = this.logs.filter(l => 
      l.user?.userName?.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      l.action?.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      l.resource?.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
    this.cdr.detectChanges();
  }
}
