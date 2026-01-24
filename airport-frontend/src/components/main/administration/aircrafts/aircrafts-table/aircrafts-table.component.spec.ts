import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { AircraftsTableComponent } from './aircrafts-table.component';
import { MatDialog } from '@angular/material/dialog';
import { AircraftService } from '../../../../../services/backend/aircraft.service';
import { of } from 'rxjs';
import { AircraftRepresentation } from '../../../../../models/aircraft.model';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('AircraftsTableComponent', () => {
    let component: AircraftsTableComponent;
    let fixture: ComponentFixture<AircraftsTableComponent>;
    let aircraftServiceSpy: jasmine.SpyObj<AircraftService>;
    let dialogSpy: jasmine.SpyObj<MatDialog>;

    const mockAircraft: AircraftRepresentation = {
        id: 1,
        aircraftName: 'Boeing 737',
        aircraftRows: 10,
        aircraftColumns: 6
    } as AircraftRepresentation;

    const mockTableData = {
        results: [mockAircraft],
        total: 1
    };

    beforeEach(async () => {
        aircraftServiceSpy = jasmine.createSpyObj('AircraftService', ['searchAircrafts']);
        dialogSpy = jasmine.createSpyObj('MatDialog', ['open']);

        aircraftServiceSpy.searchAircrafts.and.returnValue(of(mockTableData));

        await TestBed.configureTestingModule({
            imports: [
                AircraftsTableComponent,
                NoopAnimationsModule
            ],
            providers: [
                { provide: AircraftService, useValue: aircraftServiceSpy },
                { provide: MatDialog, useValue: dialogSpy }
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(AircraftsTableComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should load aircrafts table on init', () => {
        expect(aircraftServiceSpy.searchAircrafts).toHaveBeenCalled();
        expect(component.dataSource.data.length).toBe(1);
    });

    it('should reset filtering', () => {
        component.filterBy = 'aircraftName';
        component.filterValue = 'Boeing';
        component.resetFiltering();

        expect(component.filterBy).toBe('');
        expect(component.filterValue).toBe('');
        expect(aircraftServiceSpy.searchAircrafts).toHaveBeenCalledTimes(3);
    });

    it('should apply filter if filterBy and filterValue are set', () => {
        component.filterBy = 'aircraftName';
        component.filterValue = 'Boeing';
        component.applyFilter();

        expect(aircraftServiceSpy.searchAircrafts).toHaveBeenCalledTimes(3);
    });

    it('should open create modal and reload table on success', fakeAsync(() => {
        const afterClosed = of('success');
        dialogSpy.open.and.returnValue({ afterClosed: () => afterClosed } as any);

        component.openCreateModal();
        tick();

        expect(dialogSpy.open).toHaveBeenCalled();
        expect(aircraftServiceSpy.searchAircrafts).toHaveBeenCalledTimes(3);
    }));

    it('should open edit modal and reload table on success', fakeAsync(() => {
        const afterClosed = of('success');
        dialogSpy.open.and.returnValue({ afterClosed: () => afterClosed } as any);

        component.editAircraft(mockAircraft);
        tick();

        expect(dialogSpy.open).toHaveBeenCalledWith(jasmine.any(Function), { data: mockAircraft });
        expect(aircraftServiceSpy.searchAircrafts).toHaveBeenCalledTimes(3);
    }));

    it('should open delete modal and reload table on success', fakeAsync(() => {
        const afterClosed = of('success');
        dialogSpy.open.and.returnValue({ afterClosed: () => afterClosed } as any);

        component.deleteAircraft(mockAircraft);
        tick();

        expect(dialogSpy.open).toHaveBeenCalledWith(jasmine.any(Function), { data: mockAircraft });
        expect(aircraftServiceSpy.searchAircrafts).toHaveBeenCalledTimes(3);
    }));
});
