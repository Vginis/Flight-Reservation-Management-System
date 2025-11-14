import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { SearchParams } from '../../../../../models/common.models';
import { AirlineService } from '../../../../../services/backend/airline.service';
import { AirlineRepresentation } from '../../../../../models/airline.model';

@Component({
  selector: 'app-airlines-table',
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatOptionModule,
    MatSelectModule,
    MatIconModule,
    MatButtonModule,
    FormsModule
  ],
  templateUrl: './airlines-table.component.html',
  styleUrl: './airlines-table.component.css'
})
export class AirlinesTableComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ['airlineName', 'u2digitCode'];
  dataSource = new MatTableDataSource<AirlineRepresentation>([]);
  sortBy: string = 'airlineName';
  sortDirection: string = 'asc';
  filterBy: string = '';
  filterValue: string = '';
  params!: SearchParams;
  pagingOptions = [10,20,50];
  filterOptions = [{key:'airlineName',label:'Airline Name'}, {key:'u2digitCode', label: 'Code'}];
  
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private readonly airlineService: AirlineService,
  ){
    this.resetSearchParams();
  }

  ngOnInit(): void {
    this.loadAirlinesTable()
  }

  ngAfterViewInit(): void {
    this.paginator.page.subscribe(() => {
      this.loadAirlinesTable()}
    );
    this.sort.sortChange.subscribe(() => {
      this.params.sortBy = this.sort.active;
      this.params.sortDirection = this.sort.direction;
      this.loadAirlinesTable();
    });
  }

  async loadAirlinesTable(): Promise<void>{
    const page = this.paginator?.pageIndex || 0;
    const size = this.paginator?.pageSize || 10;

    this.params.index = page;
    this.params.size = size;
    this.airlineService.searchAirlines(this.params).subscribe({
      next: (data) => {
        this.dataSource.data = data.results;
        this.paginator.length = data.total;
      },
      error: (err) => {
        console.error('Failed to fetch airlines:', err);
      }
    })
  }

  resetSearchParams(): void{
    this.params = {
      searchField: '',
      searchValue: '',
      size: 10,
      index: 0,
      sortBy: 'airlineName',
      sortDirection: 'asc'
    }
    this.loadAirlinesTable();
  }

  isFilteringEmpty(): boolean{
    return this.filterBy==='' || this.filterValue==='';
  }

  resetFiltering(): void{
    this.filterBy = '';
    this.filterValue = '';
    this.resetSearchParams();
  }

  applyFilter(): void {
    if(this.filterBy!=='' && this.filterValue!==''){
      this.params.searchField = this.filterBy;
      this.params.searchValue = this.filterValue;
      this.loadAirlinesTable();
    }
  }
}
