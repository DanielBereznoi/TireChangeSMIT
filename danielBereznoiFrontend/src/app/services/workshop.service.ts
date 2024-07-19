import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Workshop } from '../models/Workshop';

@Injectable({
  providedIn: 'root'
})
export class WorkshopService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {
  }

  getAllWorkshops(): Observable<Workshop[]> {
    let httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    return this.http.get<Workshop[]>(`${this.baseUrl}/workshops`, httpOptions);
  }

  getAllVehicleTypes(): Observable<string[]> {
    let httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    return this.http.get<string[]>(`${this.baseUrl}/workshops/vehicle-types`, httpOptions);
  }
}
