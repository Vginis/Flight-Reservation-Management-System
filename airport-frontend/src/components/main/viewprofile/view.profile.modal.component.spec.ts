import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewProfileModalComponent } from './view.profile.modal.component';

describe('ViewProfileModalComponent', () => {
  let component: ViewProfileModalComponent;
  let fixture: ComponentFixture<ViewProfileModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewProfileModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewProfileModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
