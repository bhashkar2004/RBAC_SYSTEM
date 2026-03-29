import { Component, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-roles',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './roles.html',
  styleUrl: './roles.css'
})
export class RolesComponent {
  private http = inject(HttpClient);
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);
  
  roles: any[] = [];
  allPermissions: any[] = [];
  
  isModalOpen = false;
  newRole = { roleName: '', roleDescription: '' };

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadData();
    }
  }

  loadData() {
    forkJoin({
      roles: this.http.get<any[]>('http://127.0.0.1:8085/api/roles'),
      permissions: this.http.get<any[]>('http://127.0.0.1:8085/api/permissions')
    }).subscribe(data => {
      this.roles = data.roles;
      this.allPermissions = data.permissions;
      this.cdr.detectChanges();
    });
  }

  hasPermission(role: any, permissionId: number): boolean {
    return role.permissions?.some((p: any) => p.permissionId === permissionId);
  }

  togglePermission(role: any, permission: any) {
    // In a real app, this would call the backend to link/unlink
    console.log(`Toggling permission ${permission.action} for role ${role.roleName}`);
    // Mock update logic
    if (this.hasPermission(role, permission.permissionId)) {
       role.permissions = role.permissions.filter((p: any) => p.permissionId !== permission.permissionId);
    } else {
       if (!role.permissions) role.permissions = [];
       role.permissions.push(permission);
    }
  }

  saveRole() {
    this.http.post('http://127.0.0.1:8085/api/roles', this.newRole)
      .subscribe({
        next: () => {
          this.loadData();
          this.isModalOpen = false;
          this.newRole = { roleName: '', roleDescription: '' };
        },
        error: (err) => {
          console.error("Failed to create role:", err);
          alert("Failed to create role. Ensure you have the right permissions and the role doesn't already exist.");
        }
      });
  }

  deleteRole(id: number) {
    if (confirm('Delete this role? This may affect many users.')) {
      this.http.delete(`http://127.0.0.1:8085/api/roles/${id}`)
        .subscribe(() => this.loadData());
    }
  }
}
