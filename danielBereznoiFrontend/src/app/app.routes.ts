import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { BookingComponent } from './booking/booking.component';

export const routes: Routes = [
    { path: "home", component: HomeComponent },
    { path: '', redirectTo: "/home", pathMatch: 'full' },
    { path: "booking", component: BookingComponent }
];
