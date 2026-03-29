import { Component, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './users.html',
  styleUrl: './users.css'
})
export class UsersComponent {
  private http = inject(HttpClient);
  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);
  
  users: any[] = [];
  filteredUsers: any[] = [];
  searchTerm: string = '';
  
  isModalOpen = false;
  editingUser: any = null;
  userForm = { userName: '', userEmail: '', password: '', role: 'EMPLOYEE' };

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.loadUsers();
    }
  }

  loadUsers() {
    this.http.get<any[]>('http://127.0.0.1:8085/api/user')
      .subscribe({
        next: (res) => {
          console.log('API Response received in loadUsers:', res);
          this.users = res;
          this.applyFilter();
        },
        error: (err) => console.error('Failed to load users:', err)
      });
  }

  applyFilter() {
    this.filteredUsers = this.users.filter(u => {
      const nameMatch = u.userName ? u.userName.toLowerCase().includes(this.searchTerm.toLowerCase()) : false;
      const emailMatch = u.userEmail ? u.userEmail.toLowerCase().includes(this.searchTerm.toLowerCase()) : false;
      return nameMatch || emailMatch;
    });
    console.log('Filtered users length:', this.filteredUsers.length);
    this.cdr.detectChanges();
  }

  openModal(user: any = null) {
    this.editingUser = user;
    if (user) {
      this.userForm = { 
        ...user, 
        password: '', // Clear password field for edits
        role: user.roles && user.roles.length > 0 ? user.roles[0] : 'EMPLOYEE'
      };
    } else {
      this.userForm = { userName: '', userEmail: '', password: '', role: 'EMPLOYEE' };
    }
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
    this.editingUser = null;
  }

  saveUser() {
    const { roles, ...formData } = this.userForm as any;
    const payload = {
      ...formData,
      roleNames: [this.userForm.role]
    };

    if (this.editingUser) {
      this.http.put(`http://127.0.0.1:8085/api/user/${this.editingUser.userId}`, payload)
        .subscribe({
          next: (updatedUser: any) => {
            // Update local state immediately
            this.users = this.users.map(u => u.userId === this.editingUser.userId ? updatedUser : u);
            this.applyFilter();
            this.closeModal();
            this.loadUsers(); // Refresh from backend to be sure
          },
          error: (err) => console.error('Update failed:', err)
        });
    } else {
      this.http.post('http://127.0.0.1:8085/api/user', payload)
        .subscribe({
          next: (newUser: any) => {
            // Add to local state immediately
            this.users = [...this.users, newUser];
            this.applyFilter();
            this.closeModal();
            this.loadUsers(); // Refresh from backend to be sure
          },
          error: (err) => console.error('Create failed:', err)
        });
    }
  }

  deleteUser(id: number) {
    if (confirm('Are you sure you want to delete this user?')) {
      this.http.delete(`http://127.0.0.1:8085/api/user/${id}`)
        .subscribe({
          next: () => {
            // Remove from local state immediately
            this.users = this.users.filter(u => u.userId !== id);
            this.applyFilter();
            this.loadUsers(); // Refresh from backend to be sure
          },
          error: (err) => console.error('Delete failed:', err)
        });
    }
  }
}
