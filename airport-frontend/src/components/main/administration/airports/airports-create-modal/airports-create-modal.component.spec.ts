import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { AirportsCreateModalComponent } from './airports-create-modal.component';
import { FormBuilder } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { SnackbarService } from '../../../../../services/frontend/snackbar.service';
import { AirportService } from '../../../../../services/backend/airport.service';
import { CityCountryService } from '../../../../../services/backend/citycountry.service';
import { of, throwError } from 'rxjs';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('AirportsCreateModalComponent', () => {
  let component: AirportsCreateModalComponent;
  let fixture: ComponentFixture<AirportsCreateModalComponent>;

  let dialogRefSpy: jasmine.SpyObj<MatDialogRef<AirportsCreateModalComponent>>;
  let snackbarSpy: jasmine.SpyObj<SnackbarService>;
  let airportServiceSpy: jasmine.SpyObj<AirportService>;
  let cityCountrySpy: jasmine.SpyObj<CityCountryService>;

  beforeEach(async () => {
    dialogRefSpy = jasmine.createSpyObj('MatDialogRef', ['close']);
    snackbarSpy = jasmine.createSpyObj('SnackbarService', ['success', 'error']);
    airportServiceSpy = jasmine.createSpyObj('AirportService', ['createAirport']);
    cityCountrySpy = jasmine.createSpyObj('CityCountryService', ['smartSearchCountries', 'smartSearchCities']);

    await TestBed.configureTestingModule({
      imports: [
        AirportsCreateModalComponent,
        NoopAnimationsModule
      ],
      providers: [
        FormBuilder,
        { provide: MatDialogRef, useValue: dialogRefSpy },
        { provide: SnackbarService, useValue: snackbarSpy },
        { provide: AirportService, useValue: airportServiceSpy },
        { provide: CityCountryService, useValue: cityCountrySpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AirportsCreateModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values and required validators', () => {
    const form = component.airportCreateForm;
    expect(form).toBeTruthy();
    expect(form.get('airportName')?.value).toBe('');
    expect(form.get('city')?.value).toBe('');
    expect(form.get('country')?.value).toBe('');
    expect(form.get('u3digitCode')?.value).toBe('');
    expect(form.get('airportName')?.valid).toBeFalse();
  });

  it('should validate u3digitCode pattern', () => {
    const control = component.airportCreateForm.get('u3digitCode')!;
    control.setValue('AB');
    expect(control.valid).toBeFalse();
    control.setValue('ABC');
    expect(control.valid).toBeTrue();
  });

  it('should call createAirport and close dialog on success', () => {
    airportServiceSpy.createAirport.and.returnValue(of({}));
    cityCountrySpy.smartSearchCountries.and.returnValue(of(['USA']));
    cityCountrySpy.smartSearchCities.and.returnValue(of(['New York']));
    component.airportCreateForm.setValue({
      airportName: 'JFK',
      city: 'New York',
      country: 'USA',
      u3digitCode: 'JFK'
    });

    component.createNewAirport();

    expect(airportServiceSpy.createAirport).toHaveBeenCalled();
    expect(snackbarSpy.success).toHaveBeenCalledWith('Airport created successfully.');
    expect(dialogRefSpy.close).toHaveBeenCalledWith('success');
  });

  it('should call error snackbar on createAirport failure', () => {
    airportServiceSpy.createAirport.and.returnValue(
      throwError(() => ({ error: { key: 'ERR_CREATE' } }))
    );
    cityCountrySpy.smartSearchCountries.and.returnValue(of(['USA']));
    cityCountrySpy.smartSearchCities.and.returnValue(of(['New York']));
    component.airportCreateForm.setValue({
      airportName: 'JFK',
      city: 'New York',
      country: 'USA',
      u3digitCode: 'JFK'
    });

    component.createNewAirport();

    expect(snackbarSpy.error).toHaveBeenCalledWith('Airport was not created successfully! ERR_CREATE');
  });

  it('should not submit if form is invalid', () => {
        cityCountrySpy.smartSearchCountries.and.returnValue(of(['USA','UK']));
    cityCountrySpy.smartSearchCities.and.returnValue(of(['New York']));
    component.airportCreateForm.setValue({
      airportName: '',
      city: '',
      country: '',
      u3digitCode: ''
    });

    component.createNewAirport();

    expect(airportServiceSpy.createAirport).not.toHaveBeenCalled();
    expect(snackbarSpy.success).not.toHaveBeenCalled();
  });

  it('should close dialog without result', () => {
    cityCountrySpy.smartSearchCountries.and.returnValue(of(['USA','UK']));
    cityCountrySpy.smartSearchCities.and.returnValue(of(['New York']));
    component.close();
    expect(dialogRefSpy.close).toHaveBeenCalled();
  });

  it('should validate autocomplete options for country and city', fakeAsync(() => {
    cityCountrySpy.smartSearchCountries.and.returnValue(of(['USA','UK']));
    cityCountrySpy.smartSearchCities.and.returnValue(of(['New York']));
    component.airportCreateForm.get('country')?.setValue('France');
    tick(300);

    expect(component.countriesList).toEqual(['USA', 'UK']);
    const validator = component.validateAutocompleteCityCountryOption(['USA', 'UK'], 'Country');
    const controlMock = { value: 'France' } as any;
    expect(validator(controlMock)).toEqual({ invalidCountryAutoComplete: true });
  }));
});
