package lt.imantasm.s_task.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final CurrencyRatesService ratesService;

    @CrossOrigin
    @GetMapping("today-rates")
    public ResponseEntity<List<Currency>> getTodayRates(){
        return ResponseEntity.ok(this.ratesService.findAllRates());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/calculate/rate-amount")
    public ResponseEntity<ExchangeTask> calculateExchangeResult(@RequestBody ExchangeTask exchangeTask) {
        return ResponseEntity.ok(ratesService.calculateResultForExchange(exchangeTask));
    }
}
