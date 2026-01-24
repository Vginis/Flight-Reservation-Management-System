import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AircraftsDeleteModalComponent } from './aircrafts-delete-modal.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AircraftService } from '../../../../../services/backend/aircraft.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { of, throwError } from 'rxjs';
import { AircraftRepresentation } from '../../../../../models/aircraft.model';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('AircraftsDeleteModalComponent', () => {
    let component: AircraftsDeleteModalComponent;
    let fixture: ComponentFixture<AircraftsDeleteModalComponent>;

    let aircraftServiceSpy: jasmine.SpyObj<AircraftService>;
    let snackbarSpy: jasmine.SpyObj<SnackbarService>;
    let dialogRefSpy: jasmine.SpyObj<MatDialogRef<AircraftsDeleteModalComponent>>;

    const mockAircraft: AircraftRepresentation = {
        id: 1,
        aircraftName: 'Boeing 737',
        aircraftRows: 10,
        aircraftColumns: 6
    } as AircraftRepresentation;

    beforeEach(async () => {
        aircraftServiceSpy = jasmine.createSpyObj('AircraftService', ['deleteAircraft']);
        snackbarSpy = jasmine.createSpyObj('SnackbarService', ['success', 'error']);
        dialogRefSpy = jasmine.createSpyObj('MatDialogRef', ['close']);

        await TestBed.configureTestingModule({
            imports: [AircraftsDeleteModalComponent, NoopAnimationsModule],
            providers: [
                { provide: MAT_DIALOG_DATA, useValue: mockAircraft },
                { provide: AircraftService, useValue: aircraftServiceSpy },
                { provide: SnackbarService, useValue: snackbarSpy },
                { provide: MatDialogRef, useValue: dialogRefSpy }
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(AircraftsDeleteModalComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should delete aircraft successfully', () => {
        aircraftServiceSpy.deleteAircraft.and.returnValue(of({}));

        component.deleteAircraft();

        expect(aircraftServiceSpy.deleteAircraft).toHaveBeenCalledWith(mockAircraft.id);
        expect(snackbarSpy.success).toHaveBeenCalledWith('Aircraft deleted successfully');
        expect(dialogRefSpy.close).toHaveBeenCalledWith('success');
    });

    it('should show error snackbar when delete fails', () => {
        aircraftServiceSpy.deleteAircraft.and.returnValue(
            throwError(() => ({ error: { key: 'ERR_DELETE' } }))
        );

        component.deleteAircraft();

        expect(snackbarSpy.error).toHaveBeenCalledWith('Aircraft was not deleted:ERR_DELETE');
        expect(dialogRefSpy.close).not.toHaveBeenCalled();
    });

    it('should close dialog without result', () => {
        component.close();
        expect(dialogRefSpy.close).toHaveBeenCalled();
    });
});
