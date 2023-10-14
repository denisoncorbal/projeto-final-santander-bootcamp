import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';

export const authenticationGuard: CanActivateFn = (route, state) => {
  const authenticationService = inject(AuthenticationService);
  const router = inject(Router);
  const authentication = authenticationService.isAuthenticated();
  
  if(authentication)
    return true;
  
  router.navigate(['login']);
  return false;
};
