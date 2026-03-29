import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login';
import { DashboardComponent } from './features/admin/dashboard/dashboard';
import { UsersComponent } from './features/admin/users/users';
import { RolesComponent } from './features/admin/roles/roles';
import { PermissionsComponent } from './features/admin/permissions/permissions';
import { LogsComponent } from './features/admin/access-logs/access-logs';
import { HrDashboard } from './pages/hr-dashboard/hr-dashboard';
import { EmployeeDashboardComponent } from './features/employee/dashboard/employee-dashboard';
import { PartnerDashboardComponent } from './features/partner/dashboard/partner-dashboard';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'admin', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'users', component: UsersComponent, canActivate: [authGuard] },
  { path: 'roles', component: RolesComponent, canActivate: [authGuard] },
  { path: 'permissions', component: PermissionsComponent, canActivate: [authGuard] },
  { path: 'logs', component: LogsComponent, canActivate: [authGuard] },
  { path: 'hr', component: HrDashboard, canActivate: [authGuard] },
  { path: 'employee', component: EmployeeDashboardComponent, canActivate: [authGuard] },
  { path: 'partner', component: PartnerDashboardComponent, canActivate: [authGuard] },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
