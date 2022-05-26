package lt.imantasm.s_task.model.currency.service;

import java.util.List;

import lt.imantasm.s_task.model.currency.dto.ExchangeTask;
import lt.imantasm.s_task.model.currency.entity.Currency;

public interface CurrencyRatesService {

    ExchangeTask calculateResultForExchange(ExchangeTask exchangeTask);
    List<Currency> findAllRates();
}
