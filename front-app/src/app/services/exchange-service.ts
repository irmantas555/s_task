import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Currency} from 'src/app/model/currency';
import {ExchangeTask} from 'src/app/model/ExchangeTask';

@Injectable({
  providedIn: 'root'
})
export class ExchangeService{

  constructor(private http: HttpClient){}

  public getTodaysRates(): Observable<Currency[]>{
    return this.http.get<Currency[]>('http://localhost:8080/rates/today-rates');
  }

  public getExchangeResult(task : ExchangeTask): Observable<ExchangeTask>{
    return this.http.post<ExchangeTask>('http://localhost:8080/rates/calculate/rate-amount', task);
  }

}
