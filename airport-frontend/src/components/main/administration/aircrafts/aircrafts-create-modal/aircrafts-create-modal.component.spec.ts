import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AircraftsCreateModalComponent } from './aircrafts-create-modal.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AircraftService } from '../../../../../services/backend/aircraft.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { of, throwError } from 'rxjs';
import { AircraftRepresentation } from '../../../../../models/aircraft.model';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { LoadingService } from '../../../../../services/frontend/loading.service';

describe('AircraftsCreateModalComponent (create mode)', () => {
    let component: AircraftsCreateModalComponent;
    let fixture: ComponentFixture<AircraftsCreateModalComponent>;

    let aircraftServiceSpy: jasmine.SpyObj<AircraftService>;
    let snackbarSpy: jasmine.SpyObj<SnackbarService>;
    let dialogRefSpy: jasmine.SpyObj<MatDialogRef<AircraftsCreateModalComponent>>;
    let loadingServiceSpy: jasmine.SpyObj<LoadingService>

    beforeEach(async () => {
        aircraftServiceSpy = jasmine.createSpyObj('AircraftService', [
            'createAircraft',
            'updateAircraft'
        ]);

        snackbarSpy = jasmine.createSpyObj('SnackbarService', [
            'success',
            'error'
        ]);

        dialogRefSpy = jasmine.createSpyObj('MatDialogRef', ['close']);
        loadingServiceSpy = jasmine.createSpyObj('LoadingService', ['show']);

        await TestBed.configureTestingModule({
            imports: [
                AircraftsCreateModalComponent,
                NoopAnimationsModule
            ],
            providers: [
                { provide: AircraftService, useValue: aircraftServiceSpy },
                { provide: SnackbarService, useValue: snackbarSpy },
                { provide: MatDialogRef, useValue: dialogRefSpy },
                { provide: MAT_DIALOG_DATA, useValue: null }
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(AircraftsCreateModalComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should initialize in create mode', () => {
        expect(component.isEditMode).toBeFalse();
        expect(component.getLabel()).toBe('Create Aircraft');
        expect(component.getButtonLabel()).toBe('Create');
    });

    it('should update seat map correctly', () => {
        component.aircraftCreateForm.patchValue({
            aircraftRows: 5,
            aircraftColumns: 7
        });

        component.updateSeatMap();

        expect(component.seatRows.length).toBe(5);
        expect(component.leftSeatColumns.length).toBe(4);
        expect(component.rightSeatColumns.length).toBe(3);
    });

    it('should calculate aircraft capacity correctly', () => {
        component.aircraftCreateForm.patchValue({
            aircraftRows: 10,
            aircraftColumns: 6
        });

        expect(component.capacity()).toBe(60);
    });

    it('should call createAircraft on submit in create mode', () => {
        aircraftServiceSpy.createAircraft.and.returnValue(of({}));

        component.aircraftCreateForm.patchValue({
            aircraftName: 'A320',
            aircraftRows: 10,
            aircraftColumns: 6
        });

        component.createOrUpdateAircraft();

        expect(aircraftServiceSpy.createAircraft).toHaveBeenCalled();
        expect(snackbarSpy.success).toHaveBeenCalledWith(
            'Aircraft was created successfully.'
        );
        expect(dialogRefSpy.close).toHaveBeenCalledWith('success');
    });

    it('should show error snackbar when create fails', () => {
        aircraftServiceSpy.createAircraft.and.returnValue(
            throwError(() => ({ error: { key: 'ERR_CREATE' } }))
        );

        component.aircraftCreateForm.patchValue({
            aircraftName: 'A320',
            aircraftRows: 10,
            aircraftColumns: 6
        });

        component.createOrUpdateAircraft();

        expect(snackbarSpy.error).toHaveBeenCalled();
    });

    it('should not submit if form is invalid', () => {
        component.aircraftCreateForm.patchValue({
            aircraftName: '',
            aircraftRows: 0,
            aircraftColumns: 0
        });

        component.createOrUpdateAircraft();

        expect(aircraftServiceSpy.createAircraft).not.toHaveBeenCalled();
        expect(aircraftServiceSpy.updateAircraft).not.toHaveBeenCalled();
    });

    it('should close dialog without result', () => {
        component.close();
        expect(dialogRefSpy.close).toHaveBeenCalled();
    });
});

describe('AircraftsCreateModalComponent (edit mode)', () => {
    let component: AircraftsCreateModalComponent;
    let fixture: ComponentFixture<AircraftsCreateModalComponent>;

    let aircraftServiceSpy: jasmine.SpyObj<AircraftService>;
    let snackbarSpy: jasmine.SpyObj<SnackbarService>;
    let dialogRefSpy: jasmine.SpyObj<MatDialogRef<AircraftsCreateModalComponent>>;

    const mockAircraft: AircraftRepresentation = {
        id: 1,
        aircraftName: 'Boeing 737',
        aircraftRows: 10,
        aircraftColumns: 6
    } as AircraftRepresentation;

    beforeEach(async () => {
        aircraftServiceSpy = jasmine.createSpyObj('AircraftService', [
            'createAircraft',
            'updateAircraft'
        ]);

        snackbarSpy = jasmine.createSpyObj('SnackbarService', [
            'success',
            'error'
        ]);

        dialogRefSpy = jasmine.createSpyObj('MatDialogRef', ['close']);

        await TestBed.configureTestingModule({
            imports: [
                AircraftsCreateModalComponent,
                NoopAnimationsModule
            ],
            providers: [
                { provide: AircraftService, useValue: aircraftServiceSpy },
                { provide: SnackbarService, useValue: snackbarSpy },
                { provide: MatDialogRef, useValue: dialogRefSpy },
                { provide: MAT_DIALOG_DATA, useValue: mockAircraft }
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(AircraftsCreateModalComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should initialize in edit mode when aircraft is provided', () => {
        expect(component.isEditMode).toBeTrue();
        expect(component.aircraftCreateForm.value.aircraftName).toBe('Boeing 737');
    });


    it('should call updateAircraft in edit mode', () => {
        aircraftServiceSpy.updateAircraft.and.returnValue(of({}));

        fixture.detectChanges();

        component.aircraftCreateForm.patchValue({
            aircraftName: 'Updated',
            aircraftRows: 12,
            aircraftColumns: 6
        });

        component.createOrUpdateAircraft();

        expect(aircraftServiceSpy.updateAircraft).toHaveBeenCalledWith(
            jasmine.objectContaining({
                aircraftName: 'Updated',
                aircraftRows: 12,
                aircraftColumns: 6,
                aircraftCapacity: 72
            }),
            mockAircraft.id
        );

        expect(snackbarSpy.success).toHaveBeenCalledWith(
            'Aircraft was updated successfully.'
        );
        expect(dialogRefSpy.close).toHaveBeenCalledWith('success');
    });

    it('should show error snackbar when update fails', () => {
        const fixture = TestBed.createComponent(AircraftsCreateModalComponent);
        const component = fixture.componentInstance;

        fixture.detectChanges();
        component.isEditMode = true;

        aircraftServiceSpy.updateAircraft.and.returnValue(
            throwError(() => ({ error: { key: 'ERR_UPDATE' } }))
        );

        component.createOrUpdateAircraft();

        expect(snackbarSpy.error).toHaveBeenCalled();
    });
});
