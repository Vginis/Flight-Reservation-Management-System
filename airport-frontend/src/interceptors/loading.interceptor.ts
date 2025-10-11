import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { LoadingService } from "../services/frontend/loading.service";
import { finalize, Observable } from "rxjs";

@Injectable()
export class LoadingInterceptor implements HttpInterceptor{
    constructor(private readonly loadingService: LoadingService){}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        this.loadingService.show();
        return next.handle(req).pipe(finalize(() => this.loadingService.hide()));
    }
}