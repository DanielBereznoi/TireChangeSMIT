import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingRequestResponseDialogComponent } from './booking-request-response-dialog.component';

describe('BookingRequestResponseDialogComponent', () => {
  let component: BookingRequestResponseDialogComponent;
  let fixture: ComponentFixture<BookingRequestResponseDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookingRequestResponseDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookingRequestResponseDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
