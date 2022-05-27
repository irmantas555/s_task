package lt.imantasm.s_task.model.currency.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.SneakyThrows;
import lt.imantasm.s_task.model.currency.dto.ExchangeTask;
import lt.imantasm.s_task.model.currency.entity.Currency;
import lt.imantasm.s_task.model.currency.repository.CurrencyRepository;
import lt.imantasm.s_task.model.currency.service.CurrencyRatesService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class CurrencyRatesServiceImplTest {

    CurrencyRatesService service;
    ExchangeTask task;
    Currency us;
    Currency gb;
    @Mock
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void setUp() {
        task = new ExchangeTask(1L, 6L, 10L, null);
        us = new Currency(1L, "USD", "USA Dollar", BigDecimal.valueOf(1.07), LocalDate.now());
        gb = new Currency(6L, "GBP", "Great Britain Pound", BigDecimal.valueOf(0.85), LocalDate.now());
        service = new CurrencyRatesServiceImpl(currencyRepository);
    }

    @Test
    @DisplayName("Test service retrieve and values and return result if successful")
    void calculateResultForExchange() {

        when(currencyRepository.findById(1L)).thenReturn(Optional.of(us));
        when(currencyRepository.findById(6L)).thenReturn(Optional.of(gb));
        ExchangeTask task = service.calculateResultForExchange(this.task);
        assertEquals(task.getResult(), BigDecimal.valueOf(7.94));
    }

    @SneakyThrows
    @Test
    @DisplayName("Test service calculation if successful")
    void findAllRates() {
        Method gbd = service.getClass().getDeclaredMethod("getBigDecimal", ExchangeTask.class, Currency.class, Currency.class);
        gbd.setAccessible(true);
        BigDecimal target = (BigDecimal) gbd.invoke(service, task, us, gb);
        assertEquals(target, BigDecimal.valueOf(7.94));
    }
}
