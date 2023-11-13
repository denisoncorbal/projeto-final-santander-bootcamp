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
import { LogService } from '../services/log.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {


  private tryingRefresh = false;

  constructor(private authenticationService: AuthenticationService, private router: Router, private logger: LogService) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    this.logger.info("Request intercepted by jwt interceptor");
    this.logger.debug("Request", request);
    const accessToken = this.authenticationService.getAccessToken();
    this.logger.debug("AccessToken on request", accessToken);

    if (accessToken != null && accessToken != '') {
      this.logger.info("Request cloned to add accesstoken");
      const jwtReq = request.clone({
        headers: request.headers.set('Authorization', 'Bearer ' + this.authenticationService.getAccessToken()),
        withCredentials: true,
      })

      this.logger.info("Sending request cloned with accesstoken");
      return next.handle(jwtReq).pipe(
        catchError((err, caught) => {
          this.logger.info("Request with accesstoken failed");
          this.logger.error("Request with accesstoken failed", err);
          if (err.status == 403 && !this.tryingRefresh) {
            this.logger.info("Error is 403, will try to refresh token");
            this.tryingRefresh = true;
            this.authenticationService.tryRefresh().subscribe({
              next: (value) => {
                this.logger.info("Refreshed token with success");
                this.logger.debug("New tokens received", value);
                this.authenticationService.setTokens(value);
                this.logger.info("Cloning request with new accesstoken");
                const retryReq = jwtReq.clone({
                  headers: request.headers.set('Authorization', 'Bearer ' + this.authenticationService.getAccessToken()),
                  withCredentials: true,
                });
                this.logger.info("Sending request with new accesstoken");
                return next.handle(retryReq);
              },
              error: (err) => {
                this.logger.info("Refreshed token failed");
                this.logger.error("Request to refresh token failed", err);
                this.logger.info("Nullifiyng tokens");
                this.authenticationService.logout().subscribe();
                this.logger.info("Redirecting to home");
                this.router.navigate([''])

                return next.handle(err);
              },
              complete: () => {
                this.logger.info("Finishing attempt to refresh token");
                this.tryingRefresh = false;
              }
            });
            this.logger.info("After finishing attempt to refresh token");
            this.tryingRefresh = false;
          }
          this.logger.info("Error is not 403, will not try to refresh token");
          this.logger.error("Request with access token failed different from 403", err);
          return next.handle(err);
        })
      );
    } else {
      this.logger.info("There's no access token, sending request without it");
      return next.handle(request);
    }
  }
}