import {Component, OnInit} from '@angular/core';
import {ExchangeService} from 'src/app/services/exchange-service';
import {MatTableDataSource} from '@angular/material/table';
import {ActivatedRoute} from '@angular/router';
import {DialogService} from 'src/app/services/dialog-service';

@Component({
  selector: 'app-currency-list',
  templateUrl: './currency-list.component.html',
  styleUrls: ['./currency-list.component.scss']
})
export class CurrencyListComponent implements OnInit {
  currencies = new MatTableDataSource();
  columns = ['id', 'name', 'code', 'rate'];

  constructor(private exchangeService: ExchangeService, private _Activatedroute: ActivatedRoute, private dialogService: DialogService) { }

  ngOnInit(): void {
    const date = this._Activatedroute.snapshot.paramMap.get('date');
    if (!date) {
      this.exchangeService.getTodaysRates().subscribe(currenciesWithRates => {
        this.currencies.data = currenciesWithRates;
      }, () => this.dialogService.showErrorDialog('There was en error getting the result. Please try later'));
    } else {
      this.exchangeService.getDateRates(date).subscribe(currenciesWithRates => {
        this.currencies.data = currenciesWithRates;
      }, () => this.dialogService.showErrorDialog('There was en error getting the result. Please try later'));
    }
  }

  getIndex(i: string) {
    return Number(i) + 1
  }
}
