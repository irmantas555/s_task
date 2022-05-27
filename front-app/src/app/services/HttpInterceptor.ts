import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {asyncScheduler, Observable} from 'rxjs';
import {timeout} from 'rxjs/operators';

@Injectable()
export class AngularInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(timeout(3000, asyncScheduler))
  }
}
