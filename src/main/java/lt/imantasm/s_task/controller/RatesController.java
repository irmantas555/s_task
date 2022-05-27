package lt.imantasm.s_task.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lt.imantasm.s_task.model.currency.dto.ExchangeTask;
import lt.imantasm.s_task.model.currency.entity.Currency;
import lt.imantasm.s_task.model.currency.service.CurrencyRatesService;

@RestController
@RequestMapping(value = "/rates")
@RequiredArgsConstructor
public class RatesController {

    private final CurrencyRatesService currencyRatesService;

    @CrossOrigin
    @GetMapping("/today-rates")
    public ResponseEntity<List<Currency>> getTodayRates() {
        return ResponseEntity.ok(currencyRatesService.findRatesByDate(LocalDate.now()));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/calculate/rate-amount")
    public ResponseEntity<ExchangeTask> calculateExchangeResult(@RequestBody ExchangeTask exchangeTask) {
        return ResponseEntity.ok(currencyRatesService.calculateResultForExchange(exchangeTask));
    }

    @CrossOrigin()
    @GetMapping("/historical/{date}")
    public ResponseEntity<List<Currency>> calculateExchangeResult(@PathVariable("date") String stringDate) {
        LocalDate parsedDate = LocalDate.parse(stringDate);
        if (parsedDate == null) {
            throw new IllegalArgumentException("Supplied date argument is not valid");
        }
        return ResponseEntity.ok(currencyRatesService.findRatesByDate(parsedDate));
    }

    @CrossOrigin()
    @GetMapping("/historical/available-dates")
    public ResponseEntity<List<LocalDate>> calculateExchangeResult() {
        return ResponseEntity.ok(currencyRatesService.findAvailableDates());
    }

}
