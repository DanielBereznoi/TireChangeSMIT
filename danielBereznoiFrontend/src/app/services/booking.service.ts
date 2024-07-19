import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Appointment } from '../models/Appointment';
import { BookingResponse } from '../models/BookingResponse';

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {
  }


  getFilteredAppointments(encodedFilterParameters: string): Observable<Map<String, Appointment[]>> {
    let httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      params: new HttpParams().set('searchFilterDataJSON', encodedFilterParameters)
    };

    return this.http.get<{ [key: string]: Appointment[] }>(`${this.baseUrl}/appointments/filtered`, httpOptions).pipe(
      map(response => new Map(Object.entries(response)))
    );
  }

  bookATime(appointment: Appointment): Observable<BookingResponse> {
    let httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      params: new HttpParams().set('workshopAddress', appointment.workshopAddress)
    };
    return this.http.put<BookingResponse>(`${this.baseUrl}/appointments/${appointment.appointmentId}`, null, httpOptions);
  }

}
