import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AirportsDeleteModalComponent } from './airports-delete-modal.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AirportService } from '../../../../../services/backend/airport.service';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { of, throwError } from 'rxjs';
import { AirportRepresentation } from '../../../../../models/airport.models';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('AirportsDeleteModalComponent', () => {
  let component: AirportsDeleteModalComponent;
  let fixture: ComponentFixture<AirportsDeleteModalComponent>;

  let airportServiceSpy: jasmine.SpyObj<AirportService>;
  let snackbarSpy: jasmine.SpyObj<SnackbarService>;
  let dialogRefSpy: jasmine.SpyObj<MatDialogRef<AirportsDeleteModalComponent>>;

  const mockAirport: AirportRepresentation = {
    airportId: 1,
    airportName: 'JFK',
    city: 'New York',
    country: 'USA',
    u3digitCode: 'JFK'
  } as AirportRepresentation;

  beforeEach(async () => {
    airportServiceSpy = jasmine.createSpyObj('AirportService', ['deleteAirport']);
    snackbarSpy = jasmine.createSpyObj('SnackbarService', ['success', 'error']);
    dialogRefSpy = jasmine.createSpyObj('MatDialogRef', ['close']);

    await TestBed.configureTestingModule({
      imports: [AirportsDeleteModalComponent, NoopAnimationsModule],
      providers: [
        { provide: AirportService, useValue: airportServiceSpy },
        { provide: SnackbarService, useValue: snackbarSpy },
        { provide: MatDialogRef, useValue: dialogRefSpy },
        { provide: MAT_DIALOG_DATA, useValue: mockAirport }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AirportsDeleteModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should call deleteAirport and show success snackbar', () => {
    airportServiceSpy.deleteAirport.and.returnValue(of({}));

    component.deleteAirport();

    expect(airportServiceSpy.deleteAirport).toHaveBeenCalledWith(mockAirport.airportId);
    expect(snackbarSpy.success).toHaveBeenCalledWith('Airport deleted successfully');
    expect(dialogRefSpy.close).toHaveBeenCalledWith('success');
  });

  it('should show error snackbar when deleteAirport fails', () => {
    airportServiceSpy.deleteAirport.and.returnValue(
      throwError(() => ({ error: { key: 'ERR_DELETE' } }))
    );

    component.deleteAirport();

    expect(snackbarSpy.error).toHaveBeenCalledWith('Airport was not deleted:ERR_DELETE');
    expect(dialogRefSpy.close).not.toHaveBeenCalled();
  });

  it('should close dialog without result', () => {
    component.close();
    expect(dialogRefSpy.close).toHaveBeenCalled();
  });
});
