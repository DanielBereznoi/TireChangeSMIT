import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingRequestResponseDialogComponent } from './booking-request-response-dialog.component';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';

describe('BookingRequestResponseDialogComponent', () => {
  let component: BookingRequestResponseDialogComponent;
  let fixture: ComponentFixture<BookingRequestResponseDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookingRequestResponseDialogComponent],
      providers: [
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: {} }
      ]
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
