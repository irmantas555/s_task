import {Component, OnInit} from '@angular/core';
import {ExchangeService} from 'src/app/services/exchange-service';
import {Currency} from 'src/app/model/currency';
import {ExchangeTask} from 'src/app/model/ExchangeTask';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-exchage',
  templateUrl: './exchage.component.html',
  styleUrls: ['./exchage.component.scss']
})
export class ExchageComponent implements OnInit {
  currencies : Currency[] = [];
  currencyFrom: Currency | undefined;
  currencyTo: Currency | undefined;
  currentTask : ExchangeTask = new ExchangeTask();

  taskForm = new FormGroup({
    from: new FormControl(this.currentTask.from, Validators.required),
    to: new FormControl(this.currentTask.from, Validators.required),
    amount: new FormControl(this.currentTask.from, Validators.required),
  })

  constructor(private exchangeService: ExchangeService) { }

  ngOnInit(): void {
    this.exchangeService.getTodaysRates().subscribe(curenciesWithRates => {
      this.currencies.push(...curenciesWithRates);
      this.currencyFrom = this.currencies[1];
      this.currencyTo = this.currencies[1];
    });
  }

  assignFrom(currency: Currency) {
    this.currencyFrom = currency;
    this.currentTask.from = currency.id;
  }

  assignTo(currency: Currency) {
    this.currencyTo = currency;
    this.currentTask.to = currency.id;
  }

  submitTask() {
    // console.log('Before ' + JSON.stringify(this.currentTask))
    if (this.currencyFrom && this.currencyTo && this.currentTask?.amount) {
      this.exchangeService.getExchangeResult(this.currentTask).subscribe(rez => {
        console.log(rez)
        this.currentTask = rez
      })
    }
  }

  changeAmount(event: EventTarget | null) {
    const value = (event as HTMLInputElement).value
    console.log(value + ' ' + typeof value)
    this.currentTask.amount = parseFloat(value ?? '') ?? 0;
    // console.log('Before ' + JSON.stringify(this.currentTask))
  }
}
