package lt.imantasm.s_task.model.currency.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lt.imantasm.s_task.model.currency.dto.ExchangeTask;
import lt.imantasm.s_task.model.currency.entity.Currency;
import lt.imantasm.s_task.model.currency.repository.CurrencyRepository;
import lt.imantasm.s_task.model.currency.service.CurrencyRatesService;

@Service
@RequiredArgsConstructor
public class CurrencyRatesServiceImpl implements CurrencyRatesService {

    private final CurrencyRepository currencyRepository;
    @Override
    public ExchangeTask calculateResultForExchange(ExchangeTask exchangeTask) {
        Currency baseCurrency = currencyRepository.findById(exchangeTask.getFrom()).orElse(null);
        Currency targetCurrency = currencyRepository.findById(exchangeTask.getTo()).orElse(null);
        if (baseCurrency != null && targetCurrency != null) {
            BigDecimal result = getBigDecimal(exchangeTask, baseCurrency, targetCurrency);
            exchangeTask.setResult(result);
            return exchangeTask;
        } else {
            throw new IllegalArgumentException("Requested currency not found");
        }
    }

    @Override
    public List<Currency> findAllRates() {
        return currencyRepository.findAll();
    }

    private BigDecimal getBigDecimal(ExchangeTask exchangeTask, Currency baseCurrency, Currency targetCurrency) {
        BigDecimal conversastionRate = targetCurrency.getRateToEuro().divide(baseCurrency.getRateToEuro(), 4, RoundingMode.HALF_UP);
        return BigDecimal.valueOf(exchangeTask.getAmount().doubleValue() * conversastionRate.doubleValue()).setScale(2, RoundingMode.HALF_UP);
    }
}
