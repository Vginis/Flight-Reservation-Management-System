import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import {MatIconModule} from '@angular/material/icon';
import {provideNativeDateAdapter} from '@angular/material/core';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-flightsearch',
  standalone: true,
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
    MatIconModule,
    MatDividerModule
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
    });
  }

  onSubmit() {
    console.log(this.searchForm.value);
  }

  disablePastDates = (date: Date | null): boolean => {
    if(!date) return false;
    const today = new Date();
    today.setHours(0,0,0,0);
    return date >= today;
  };
}
