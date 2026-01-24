import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewReservationDetailsModalComponent } from './view-reservation-details-modal.component';

describe('ViewReservationDetailsModalComponent', () => {
  let component: ViewReservationDetailsModalComponent;
  let fixture: ComponentFixture<ViewReservationDetailsModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewReservationDetailsModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewReservationDetailsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
