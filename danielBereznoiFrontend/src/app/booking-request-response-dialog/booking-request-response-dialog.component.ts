import { Component, inject, model } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { DialogData } from '../models/DialogData';

@Component({
  selector: 'app-booking-request-response-dialog',
  standalone: true,
  imports: [MatDialogModule],
  templateUrl: './booking-request-response-dialog.component.html',
  styleUrl: './booking-request-response-dialog.component.scss'
})
export class BookingRequestResponseDialogComponent {
  readonly dialogRef = inject(MatDialogRef<BookingRequestResponseDialogComponent>);
  readonly data = inject<DialogData>(MAT_DIALOG_DATA);
  readonly message = model(this.data.message);

  onNoClick(): void {
    this.dialogRef.close();
  }
}
