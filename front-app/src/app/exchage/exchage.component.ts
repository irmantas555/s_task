import {Component, OnInit} from '@angular/core';
import {ExchangeService} from 'src/app/services/exchange-service';
import {Currency} from 'src/app/model/currency';
import {ExchangeTask} from 'src/app/model/ExchangeTask';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {DialogService} from 'src/app/services/dialog-service';

@Component({
  selector: 'app-exchage',
  templateUrl: './exchage.component.html',
  styleUrls: ['./exchage.component.scss']
})
export class ExchageComponent implements OnInit {
  currencies: Currency[] = [];
  task = new ExchangeTask()

  taskForm = this.fb.group({
    from: new FormControl('', Validators.required),
    to: new FormControl('', Validators.required),
    amount: new FormControl(0, [Validators.required, Validators.min(0.01)]),
  })

  constructor(private exchangeService: ExchangeService, private fb: FormBuilder, private dialogService: DialogService) { }

  ngOnInit(): void {
    this.exchangeService.getTodaysRates().subscribe(curenciesWithRates => {
      this.currencies.push(...curenciesWithRates);
      this.taskForm.setValue({from: this.currencies[0], to: this.currencies[1], amount: 0});
      this.assignFrom();
      this.assignTo();
    }, () => this.dialogService.showErrorDialog('There was en error getting the result. Please try later'));
  }

  assignFrom() {
    this.task.from = this.taskForm.controls['from'].value.id;
    this.task.result = undefined;
  }

  assignTo() {
    this.task.to = this.taskForm.controls['to'].value.id;
    this.task.result = undefined;
  }

  submitTask() {
    if (this.taskForm.valid) {
      this.exchangeService.getExchangeResult(this.task).subscribe(rez => {
        this.task = rez
      });
    }
  }

  changeAmount(event: EventTarget | null) {
    const value = (event as HTMLInputElement).value;
    this.task.amount = parseFloat(value ?? '') ?? 0;
  }

}
