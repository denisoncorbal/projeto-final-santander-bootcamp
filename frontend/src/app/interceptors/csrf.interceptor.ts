import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpXsrfTokenExtractor
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, of } from 'rxjs';

@Injectable()
export class CsrfInterceptor implements HttpInterceptor {

  constructor(private csrfExtract: HttpXsrfTokenExtractor) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (request.method != "GET") {
      // Get csrf from the cookie.
      console.log('Extract token = ' + this.csrfExtract.getToken());
      const csrf = this.csrfExtract.getToken()?.toString() || '';
      // Clone the request and replace the original headers with
      // cloned headers, updated with csrf.
      const csrfReq = request.clone({
        headers: request.headers.set('X-XSRF-TOKEN', csrf),
        withCredentials: true,
      })

      return next.handle(csrfReq).pipe(catchError((err, caught) => {
        console.log("Error to retry: " + err);
        if (err.status == 403 && csrf == '') {
          console.log("Will retry");
          console.log('Extract token = ' + this.csrfExtract.getToken());
          const csrf = this.csrfExtract.getToken()?.toString() || '';
          // Clone the request and replace the original headers with
          // cloned headers, updated with csrf.
          const retryReq = request.clone({
            headers: request.headers.set('X-XSRF-TOKEN', csrf),
            withCredentials: true,
          })
          return next.handle(retryReq);
        }
        return of(err);
      }));
    }
    return next.handle(request);
  }
}
