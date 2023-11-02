import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, of } from 'rxjs';
import { AuthenticationService } from '../services/authentication.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    const jwtReq = request.clone({
      headers: request.headers.set('Authorization', 'Bearer ' + this.authenticationService.getAccessToken()),
      withCredentials: true,
    })

    return next.handle(jwtReq).pipe(catchError((err, caught) => {
      if (err.status == 403) {
        this.authenticationService.tryRefresh().add(
          () => {
            const retryReq = request.clone({
              headers: request.headers.set('Authorization', 'Bearer ' + this.authenticationService.getAccessToken()),
              withCredentials: true,
            })
            return next.handle(retryReq);
          }
        )
      }
      return of(err);
    }));
  }
}