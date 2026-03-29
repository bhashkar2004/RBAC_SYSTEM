import { Component, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-hr-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './hr-dashboard.html',
  styleUrl: './hr-dashboard.css',
})
export class HrDashboard {
  private http = inject(HttpClient);
  
  employees: any[] = [];
  roles: any[] = [];
  selectedEmployee: any = null;
  isAddModalOpen = false;
  userForm = { userName: '', userEmail: '', password: 'password', role: 'EMPLOYEE' };

  openProfile(employee: any) {
    this.selectedEmployee = employee;
  }

  closeProfile() {
    this.selectedEmployee = null;
  }

  openAddUserModal() {
    this.userForm = { userName: '', userEmail: '', password: 'password', role: 'EMPLOYEE' };
    this.isAddModalOpen = true;
  }

  closeAddModal() {
    this.isAddModalOpen = false;
  }

  saveUser() {
    const payload = {
      userName: this.userForm.userName,
      userEmail: this.userForm.userEmail,
      password: this.userForm.password,
      roleNames: [this.userForm.role],
      userType: 'EMPLOYEE',
      userStatus: 'ACTIVE'
    };
    
    this.http.post('http://127.0.0.1:8085/api/user', payload)
      .subscribe({
        next: (newUser: any) => {
          this.employees.push(newUser);
          this.closeAddModal();
        },
        error: (err) => alert('Failed to create user. Please check your inputs.')
      });
  }

  toggleStatus(emp: any) {
    const newStatus = emp.userStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    // Match the backend DTO structure
    const payload = {
      ...emp,
      userStatus: newStatus,
      roleNames: emp.roles && emp.roles.length > 0 ? emp.roles : [emp.role]
    };
    
    this.http.put(`http://127.0.0.1:8085/api/user/${emp.userId}`, payload)
      .subscribe({
        next: () => {
          emp.userStatus = newStatus;
        },
        error: (err) => console.error('Failed to update status', err)
      });
  }

  ngOnInit() {
    forkJoin({
      users: this.http.get<any[]>('http://127.0.0.1:8085/api/user').pipe(catchError(() => of([]))),
      roles: this.http.get<any[]>('http://127.0.0.1:8085/api/roles').pipe(catchError(() => of([])))
    }).subscribe(data => {
      this.employees = data.users.map((u: any) => {
        u.role = u.roles && u.roles.length > 0 ? (u.roles[0].roleName || u.roles[0]) : 'NONE';
        return u;
      });
      this.roles = data.roles;
    });
  }

  updateRole(employee: any) {
    const payload = {
      ...employee,
      roleNames: [employee.role]
    };
    this.http.put(`http://127.0.0.1:8085/api/user/${employee.userId}`, payload)
      .subscribe(() => console.log(`Updated role for ${employee.userName}`));
  }
}
