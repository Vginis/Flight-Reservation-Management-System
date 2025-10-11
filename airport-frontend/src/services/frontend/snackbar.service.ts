import { Injectable } from "@angular/core";
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
@Injectable({ providedIn: 'root' })
export class SnackbarService{
    constructor(private readonly snackBar: MatSnackBar) {}

    private readonly defaultConfig: MatSnackBarConfig = {
        duration: 3000,
        horizontalPosition: 'center',
        verticalPosition: 'bottom'
    }

    success(message: string, action: string = 'OK', config?: MatSnackBarConfig){
        this.snackBar.open(message, action, {
            ...this.defaultConfig,
            panelClass: ['snackbar-success'],
            ...config
        });
    }

    error(message: string, action: string = 'Close', config?: MatSnackBarConfig){
        this.snackBar.open(message, action, {
            ...this.defaultConfig,
            panelClass: ['snackbar-error'],
            ...config
        });
    }

    info(message: string, action: string = 'OK', config?: MatSnackBarConfig){
        this.snackBar.open(message, action, {
            ...this.defaultConfig,
            panelClass: ['snackbar-info'],
            ...config
        });
    }
}