import { ChangeDetectionStrategy, Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule, provideNativeDateAdapter } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';

@Component({
    selector: 'app-flightsearch',
    providers: [provideNativeDateAdapter()],
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatSelectModule,
        MatInputModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatButtonModule,
        MatDatepickerModule,
        MatIconModule
    ],
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './flightsearch.component.html',
    styleUrls: ['./flightsearch.component.css']
})
export class FlightsearchComponent {
  searchForm: FormGroup;

  cities = [
    { city: 'Athens', code: 'ATH'},
    { city: 'Thessaloniki', code: 'SKG'}
  ];

  constructor(private fb: FormBuilder) {
    this.searchForm = this.fb.group({
      from: [''],
      to: [''],
      departing: [''],
      returning: ['']
    }, 
    { validators: [this.routeValidator]});
  }

  onSubmit() {
      console.log('Form submitted', this.searchForm.value);
  }

  disablePastDates = (date: Date | null): boolean => {
    if(!date) return false;
    const today = new Date();
    today.setHours(0,0,0,0);
    return date >= today;
  };

  routeValidator(control: AbstractControl): ValidationErrors | null {
    const from = control.get('from')?.value;
    const to = control.get('to')?.value;

    if (from && to && from.code === to.code) {
      return { sameRoute: true };
    }
    return null;
  }

  validateArrivalDates = (date: Date | null): boolean => {
    if(!date) return false;
    const { departing } = this.searchForm.value;
    if(departing !== ''){
      return date >= departing
    }

    return this.disablePastDates(date);
  }

  onDepartingSelected = (e: any): void => {
    const { returning } = this.searchForm.value;
    const selectedDate= e.value;
    if(returning!== '' && returning < selectedDate){
      this.searchForm.patchValue({ returning: '' });
    }
  }

  atLeastOneSelected = (): boolean => {
    const { from, to, departing, returning } = this.searchForm.value;
    return !!(from || to || departing || returning);
  }

  formIsNotValid = (): boolean => {
    return !this.atLeastOneSelected() || this.searchForm.invalid;
  }
}
