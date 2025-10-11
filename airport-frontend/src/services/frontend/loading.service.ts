import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class LoadingService {
    private readonly _loading = new BehaviorSubject<boolean>(false);
    public loading$ = this._loading.asObservable();

    constructor() {};

    show(): void{
        this._loading.next(true);
    }

    hide(): void {
        this._loading.next(false);
    }
}