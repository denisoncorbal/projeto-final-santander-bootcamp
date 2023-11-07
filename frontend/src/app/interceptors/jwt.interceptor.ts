import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, catchError } from 'rxjs';
import { AuthenticationService } from '../services/authentication.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  private tryingRefresh = false;

  constructor(private authenticationService: AuthenticationService, private router: Router) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const accessToken = this.authenticationService.getAccessToken();

    if (accessToken != null && accessToken != '') {
      const jwtReq = request.clone({
        headers: request.headers.set('Authorization', 'Bearer ' + this.authenticationService.getAccessToken()),
        withCredentials: true,
      })

      return next.handle(jwtReq).pipe(
        catchError((err, caught) => {
          if (err.status == 403 && !this.tryingRefresh) {
            this.tryingRefresh = true;
            this.authenticationService.tryRefresh().subscribe({
              next: (value) => {
                this.authenticationService.setTokens(value);

                const retryReq = jwtReq.clone({
                  headers: request.headers.set('Authorization', 'Bearer ' + this.authenticationService.getAccessToken()),
                  withCredentials: true,
                });

                return next.handle(retryReq);
              },
              error: (err) => {
                this.authenticationService.logout();

                this.router.navigate([''])

                return next.handle(err);
              },
              complete: () => {
                this.tryingRefresh = false;
              }
            });
          }
          return next.handle(err);
        })
      );
    } else {
      return next.handle(request);
    }
  }
}