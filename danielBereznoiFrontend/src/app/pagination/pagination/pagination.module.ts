import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PaginatorComponent } from '../../paginator/paginator.component';



@NgModule({
  declarations: [],
  imports: [
    CommonModule, PaginatorComponent
  ],
  exports: [PaginatorComponent]
})
export class PaginationModule { }
