import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-car-list',
  templateUrl: './car-list.component.html',
  styleUrls: ['./car-list.component.css']
})
export class CarListComponent implements OnInit {
  cars: any[] = [];

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.fetchCars();
  }

  fetchCars(): void {
    fetch('/api/cars')
      .then(response => response.json())
      .then(data => this.cars = data);
  }

  viewCar(car: any): void {
    this.router.navigate(['/car-details', car]);
  }
}
