import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { AirportsTableComponent } from './airports-table.component';
import { MatDialog } from '@angular/material/dialog';
import { AirportService } from '../../../../../services/backend/airport.service';
import { of } from 'rxjs';
import { AirportRepresentation } from '../../../../../models/airport.models';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';

describe('AirportsTableComponent', () => {
  let component: AirportsTableComponent;
  let fixture: ComponentFixture<AirportsTableComponent>;

  let airportServiceSpy: jasmine.SpyObj<AirportService>;
  let matDialogSpy: jasmine.SpyObj<MatDialog>;

  const mockAirports: AirportRepresentation[] = [
    { airportId: 1, airportName: 'JFK', city: 'New York', country: 'USA', u3digitCode: 'JFK' },
    { airportId: 2, airportName: 'LHR', city: 'London', country: 'UK', u3digitCode: 'LHR' }
  ];

  beforeEach(async () => {
    airportServiceSpy = jasmine.createSpyObj('AirportService', ['searchAirports']);
    matDialogSpy = jasmine.createSpyObj('MatDialog', ['open']);

    airportServiceSpy.searchAirports.and.returnValue(of({ results: mockAirports, total: 2 }));

    await TestBed.configureTestingModule({
      imports: [
        AirportsTableComponent,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        FormsModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: AirportService, useValue: airportServiceSpy },
        { provide: MatDialog, useValue: matDialogSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AirportsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should load airports table on init', () => {
    expect(airportServiceSpy.searchAirports).toHaveBeenCalled();
    expect(component.dataSource.data.length).toBe(2);
    expect(component.paginator.length).toBe(2);
  });

  it('should apply filter and reload table', () => {
    component.filterBy = 'airportName';
    component.filterValue = 'JFK';
    component.applyFilter();

    expect(component.params.searchField).toBe('airportName');
    expect(component.params.searchValue).toBe('JFK');
    expect(airportServiceSpy.searchAirports).toHaveBeenCalledTimes(3);
  });

  it('should reset filtering', () => {
    component.filterBy = 'city';
    component.filterValue = 'London';

    component.resetFiltering();

    expect(component.filterBy).toBe('');
    expect(component.filterValue).toBe('');
    expect(airportServiceSpy.searchAirports).toHaveBeenCalledTimes(3);
  });

  it('should open create modal and reload table on success', fakeAsync(() => {
    const afterClosedSpy = jasmine.createSpyObj({ subscribe: (fn: any) => fn('success') });
    matDialogSpy.open.and.returnValue({ afterClosed: () => afterClosedSpy } as any);

    component.openCreateModal();
    tick();

    expect(matDialogSpy.open).toHaveBeenCalled();
    expect(airportServiceSpy.searchAirports).toHaveBeenCalledTimes(2);
  }));

  it('should open edit modal and reload table on success', fakeAsync(() => {
    const afterClosedSpy = jasmine.createSpyObj({ subscribe: (fn: any) => fn('success') });
    matDialogSpy.open.and.returnValue({ afterClosed: () => afterClosedSpy } as any);

    component.editAirport(mockAirports[0]);
    tick();

    expect(matDialogSpy.open).toHaveBeenCalledWith(jasmine.any(Function), { data: mockAirports[0] });
    expect(airportServiceSpy.searchAirports).toHaveBeenCalledTimes(2);
  }));

  it('should open delete modal and reload table on success', fakeAsync(() => {
    const afterClosedSpy = jasmine.createSpyObj({ subscribe: (fn: any) => fn('success') });
    matDialogSpy.open.and.returnValue({ afterClosed: () => afterClosedSpy } as any);

    component.deleteAirport(mockAirports[0]);
    tick();

    expect(matDialogSpy.open).toHaveBeenCalledWith(jasmine.any(Function), { data: mockAirports[0] });
    expect(airportServiceSpy.searchAirports).toHaveBeenCalledTimes(2);
  }));

  it('should detect if filtering is empty', () => {
    component.filterBy = '';
    component.filterValue = '';
    expect(component.isFilteringEmpty()).toBeTrue();

    component.filterBy = 'airportName';
    component.filterValue = '';
    expect(component.isFilteringEmpty()).toBeTrue();

    component.filterBy = 'airportName';
    component.filterValue = 'JFK';
    expect(component.isFilteringEmpty()).toBeFalse();
  });
});
