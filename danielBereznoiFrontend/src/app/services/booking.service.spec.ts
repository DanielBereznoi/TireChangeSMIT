import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Appointment } from '../models/Appointment';
import { BookingService } from './booking.service';

describe('BookingService', () => {
  let service: BookingService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [BookingService]
    });
    service = TestBed.inject(BookingService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  /*
 describe('get bookings', () => {
    it('should get booking time list in date range', () => {
      let data: Map<String, Appointment[]> | undefined;
      let filter = new Map();
      filter.set('from', '2024-01-01');
      filter.set('until', '2024-12-30');
      let encoded = encodeURIComponent(JSON.stringify(Object.fromEntries(filter)));

      service.getFilteredAppointments(encoded).subscribe((res: Map<String, Appointment[]>) => {
        data = res;
      });

      const lst: Appointment[] = [
        new Appointment('1', 'name', 'address', '2024-01-12', '11:00', ['Soiduauto', 'Veoauto']),
        new Appointment('1', 'name', 'address', '2024-01-12', '11:00', ['Soiduauto', 'Veoauto']),
        new Appointment('1', 'name', 'address', '2024-01-12', '11:00', ['Soiduauto', 'Veoauto'])
      ];

      const result = new Map();
      result.set('2024-12-30', lst);

      const req = httpTestingController.expectOne(`http://localhost:8080/api/appointments/filtered?searchFilterDataJSON=${encoded}`);
      req.flush(result);

      expect(data).toEqual(result);
    });
  });
 
  */

  
});
