import { Component, HostListener, inject } from '@angular/core';
import { Appointment } from '../models/Appointment';
import { BookingService } from '../services/booking.service';
import { CommonModule } from '@angular/common';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatDatepickerInputEvent, MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Workshop } from '../models/Workshop';
import { WorkshopService } from '../services/workshop.service';
import { MatCheckboxChange, MatCheckboxModule } from '@angular/material/checkbox';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatDialog } from '@angular/material/dialog';
import { BookingRequestResponseDialogComponent } from '../booking-request-response-dialog/booking-request-response-dialog.component';

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [
    CommonModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    FormsModule,
    ReactiveFormsModule,
    MatCheckboxModule,
    MatExpansionModule
  ],
  providers: [BookingService, provideNativeDateAdapter()],
  templateUrl: './booking.component.html',
  styleUrl: './booking.component.scss'
})
export class BookingComponent {

  data: Map<String, Appointment[]> = new Map<String, Appointment[]>();
  currentPage: number = 0;
  total = 0;
  limit = 2;
  pageEvent: PageEvent = new PageEvent;
  pageSizeOptions: number[] = [3, 5, 10];
  screenWidth: number = 1920;

  singleDateChoice = false;
  dateFrom = null;
  singleDate: Date = new Date();
  dateRange = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });

  workshops: Map<Workshop, boolean> = new Map;
  selectedWorkshops: string[] = [];

  vehicleTypes: Map<string, boolean> = new Map;
  selectedVehicleTypes: string[] = [];

  readonly dialog = inject(MatDialog);

  ngOnInit(): void {

    this.total = this.data.size;
  }

  constructor(private boookingService: BookingService, private workshopService: WorkshopService) {
    this.getScreenSize();

    this.loadSettingsFromLocalStorage();
    this.workshopService.getAllWorkshops().subscribe((res: Workshop[]) => {
      const storedAddresses = this.getStoredArray('selectedWorkshops');
      res.forEach(w => this.workshops.set(w, storedAddresses.includes(w.address)));
    });

    this.workshopService.getAllVehicleTypes().subscribe((res: string[]) => {
      const storedVehicleTypes = this.getStoredArray('selectedVehicleTypes');
      res.forEach(vehicleType => this.vehicleTypes.set(vehicleType, storedVehicleTypes.includes(vehicleType)));
    });

    this.getFilteredAppointments();

  }

  loadSettingsFromLocalStorage() {
    this.singleDateChoice = localStorage.getItem('singleDateChoice') === 'true';
    this.selectedWorkshops = this.getStoredArray('selectedWorkshops');
    this.selectedVehicleTypes = this.getStoredArray('selectedVehicleTypes');
    this.singleDate = new Date(localStorage.getItem('singleDate') || new Date().toDateString());
    this.dateRange.get('start')?.patchValue(new Date(localStorage.getItem('startDate') || new Date().toDateString()));
    this.dateRange.get('end')?.patchValue(new Date(localStorage.getItem('endDate') || '2040-12-31'));
  }

  private getStoredArray(key: string): string[] {
    const stored = localStorage.getItem(key);
    return stored ? JSON.parse(stored) : [];
  }

  @HostListener('window:resize', ['$event'])
  getScreenSize(event?: any) {
    this.screenWidth = window.innerWidth;
  }

  bookATime(appointment: Appointment) {
    this.boookingService.bookATime(appointment).subscribe(res => {
      const dialogRef = this.dialog.open(BookingRequestResponseDialogComponent, {
        data: { message: res.message }
      });

      dialogRef.afterClosed().subscribe(() => {
        window.location.reload();
      });
    });
  }

  dateCustomFormatting(date: Date) {
    const padStart = (value: number): string => value.toString().padStart(2, '0');
    return `${padStart(date.getFullYear())}-${padStart(date.getMonth() + 1)}-${padStart(date.getDate())}`;
  }

  setPageSizeOptions(setPageSizeOptionsInput: string) {
    if (setPageSizeOptionsInput) {
      this.pageSizeOptions = setPageSizeOptionsInput.split(',').map(str => +str);
    }
  }

  changePage(e: PageEvent) {
    this.pageEvent = e;
    this.currentPage = e.pageIndex;
    this.total = e.length;
    this.limit = e.pageSize;
    this.currentPage = e.pageIndex;
  }

  chooseSingleDate(event: MatDatepickerInputEvent<Date>) {
    if (event.value != null) {
      localStorage.setItem("singleDate", event.value.toDateString());
      this.getFilteredAppointments();
      window.location.reload();
    } else {
      localStorage.removeItem("singleDate");
    }
  }

  chooseStartDate(event: MatDatepickerInputEvent<Date>) {
    
    if (event.value != null) {
      localStorage.setItem("startDate", this.dateCustomFormatting(event.value));
    } else {
      localStorage.removeItem("startDate");
    }
  }
  
  chooseEndDate(event: MatDatepickerInputEvent<Date>) {
    if (event.value != null) {
      localStorage.setItem("endDate", this.dateCustomFormatting(event.value));
      if (localStorage.getItem("startDate") == null) {
        localStorage.setItem("startDate", this.dateCustomFormatting(event.value));
      }
      this.getFilteredAppointments();
      window.location.reload();
    } else {
      localStorage.removeItem("endDate");
    }
  }

  checkAWorkshop(event: MatCheckboxChange, address: string) {
    this.updateSelection(event.checked, address, 'selectedWorkshops', this.selectedWorkshops);
  }

  checkVehilceType(event: MatCheckboxChange, vehicleType: any) {
    this.updateSelection(event.checked, vehicleType, 'selectedVehicleTypes', this.selectedVehicleTypes);

  }

  private updateSelection(checked: boolean, item: string, storageKey: string, selectedArray: string[]) {
    if (checked) {
      selectedArray.push(item);
    } else {
      const index = selectedArray.indexOf(item);
      if (index > -1) selectedArray.splice(index, 1);
    }
    localStorage.setItem(storageKey, JSON.stringify(selectedArray));
    this.getFilteredAppointments();
    window.location.reload();
  }

  getFilteredAppointments() {
    let filter = new Map;
    if (this.selectedVehicleTypes.length > 0) filter.set("vehicleTypes", this.selectedVehicleTypes);
    
    if (this.selectedWorkshops.length > 0) filter.set("workshopAddresses", this.selectedWorkshops);
    
    if (this.singleDateChoice) {
      let date = this.dateCustomFormatting(this.singleDate);
      filter.set("from", date);
      filter.set("until", date);
    } else {
      filter.set("from", this.dateCustomFormatting(this.dateRange.get("start")!.value!));
      filter.set("until", this.dateCustomFormatting(this.dateRange.get("end")!.value!));
    }
    let filterString = JSON.stringify(Object.fromEntries(filter));
    this.boookingService.getFilteredAppointments(encodeURIComponent(filterString)).subscribe((res: Map<String, Appointment[]>) => {
      this.data = res;
      this.ngOnInit();
    })
  }

  changeSingleDateChoice(event: MatCheckboxChange) {
    this.singleDateChoice = event.checked;
    if (event.checked) {
      localStorage.setItem("singleDateChoice", String(true));
    } else {
      localStorage.setItem("singleDateChoice", String(false));
    }
  }
}
