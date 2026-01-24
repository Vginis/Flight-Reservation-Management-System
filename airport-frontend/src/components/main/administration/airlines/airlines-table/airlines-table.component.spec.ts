import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AirlinesTableComponent } from './airlines-table.component';
import { AirlineService } from '../../../../../services/backend/airline.service';
import { of } from 'rxjs';
import { AirlineRepresentation } from '../../../../../models/airline.model';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

describe('AirlinesTableComponent', () => {
  let component: AirlinesTableComponent;
  let fixture: ComponentFixture<AirlinesTableComponent>;
  let airlineServiceSpy: jasmine.SpyObj<AirlineService>;

  const mockAirline: AirlineRepresentation = {
    id: 1,
    airlineName: 'Delta',
    u2digitCode: 'DL'
  } as AirlineRepresentation;

  const mockTableData = {
    results: [mockAirline],
    total: 1
  };

  beforeEach(async () => {
    airlineServiceSpy = jasmine.createSpyObj('AirlineService', ['searchAirlines']);
    airlineServiceSpy.searchAirlines.and.returnValue(of(mockTableData));

    await TestBed.configureTestingModule({
      imports: [
        NoopAnimationsModule,
        AirlinesTableComponent
      ],
      providers: [
        { provide: AirlineService, useValue: airlineServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AirlinesTableComponent);
    component = fixture.componentInstance;

    component.paginator = { pageIndex: 0, pageSize: 10, length: 0, page: of({}) } as MatPaginator;
    component.sort = { active: 'airlineName', direction: 'asc', sortChange: of({}) } as MatSort;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load airlines table on init', () => {
    expect(airlineServiceSpy.searchAirlines).toHaveBeenCalled();
    expect(component.dataSource.data.length).toBe(1);
  });

  it('should reset filtering', () => {
    component.filterBy = 'airlineName';
    component.filterValue = 'Delta';
    component.resetFiltering();

    expect(component.filterBy).toBe('');
    expect(component.filterValue).toBe('');
    expect(airlineServiceSpy.searchAirlines).toHaveBeenCalledTimes(3);
  });

  it('should apply filter if filterBy and filterValue are set', () => {
    component.filterBy = 'airlineName';
    component.filterValue = 'Delta';
    component.applyFilter();

    expect(airlineServiceSpy.searchAirlines).toHaveBeenCalledTimes(3);
    expect(component.params.searchField).toBe('airlineName');
    expect(component.params.searchValue).toBe('Delta');
  });

  it('should return true for isFilteringEmpty when no filter', () => {
    component.filterBy = '';
    component.filterValue = '';
    expect(component.isFilteringEmpty()).toBeTrue();
  });

  it('should return false for isFilteringEmpty when filter is set', () => {
    component.filterBy = 'airlineName';
    component.filterValue = 'Delta';
    expect(component.isFilteringEmpty()).toBeFalse();
  });

  it('should reset search params and call loadAirlinesTable', () => {
    component.resetSearchParams();
    expect(component.params.index).toBe(0);
    expect(component.params.size).toBe(10);
    expect(component.params.sortBy).toBe('airlineName');
    expect(component.params.sortDirection).toBe('asc');
    expect(airlineServiceSpy.searchAirlines).toHaveBeenCalledTimes(3);
  });
});
