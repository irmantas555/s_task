import {Component, OnInit} from '@angular/core';
import {ExchangeService} from 'src/app/services/exchange-service';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-currency-list',
  templateUrl: './currency-list.component.html',
  styleUrls: ['./currency-list.component.scss']
})
export class CurrencyListComponent implements OnInit {
  currencies = new MatTableDataSource();
  columns = ['id', 'name', 'code', 'rate'];

  constructor(private exchangeService: ExchangeService) { }

  ngOnInit(): void {
    this.exchangeService.getTodaysRates().subscribe(currenciesWithRates => {
      this.currencies.data = currenciesWithRates;
    });
  }
}
