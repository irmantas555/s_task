import {Component, OnInit} from '@angular/core';
import {ExchangeService} from 'src/app/services/exchange-service';
import {DialogService} from 'src/app/services/dialog-service';

@Component({
  selector: 'app-historical-data',
  templateUrl: './historical-data.component.html',
  styleUrls: ['./historical-data.component.scss']
})
export class HistoricalDataComponent implements OnInit {

  dates: Date[] = [];

  constructor(private exchangeService: ExchangeService, private dialogService: DialogService) { }

  ngOnInit(): void {
    this.exchangeService.getAvailableDates().subscribe(result => {
      this.dates = result
    }, () => this.dialogService.showErrorDialog('There was en error getting the result. Please try later'))
  }

}
