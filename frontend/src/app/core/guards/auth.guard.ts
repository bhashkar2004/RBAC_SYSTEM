import { inject, PLATFORM_ID } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);

  if (!isPlatformBrowser(platformId)) {
    return true; // Allow initial SSR to pass, browser will re-check
  }

  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  if (token) {
    if (state.url.startsWith('/admin') && role !== 'ADMIN' && role !== 'IT') {
        if (role === 'HR') router.navigate(['/hr']);
        else if (role === 'PARTNER') router.navigate(['/partner']);
        else router.navigate(['/employee']);
        return false;
    }
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};
