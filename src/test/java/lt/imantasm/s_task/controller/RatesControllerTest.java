package lt.imantasm.s_task.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lt.imantasm.s_task.model.currency.dto.ExchangeTask;
import lt.imantasm.s_task.model.currency.entity.Currency;
import lt.imantasm.s_task.model.currency.service.CurrencyRatesService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RatesControllerTest {

    @Autowired
    CurrencyRatesService currencyRatesService;
    ObjectMapper mapper = new ObjectMapper();
    ExchangeTask task;
    Currency us;
    Currency gb;
    @InjectMocks
    private RatesController ratesController;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        task = new ExchangeTask(1L, 6L, 10L, null);
        us = new Currency(1L, "USD", "USA Dollar", BigDecimal.valueOf(1.07));
        gb = new Currency(6L, "GBP", "Great Britain Pound", BigDecimal.valueOf(0.85));
    }

    @SneakyThrows
    @Test
    void getTodayRates() {
        mockMvc.perform(get("/rates/today-rates"))
               .andExpect(jsonPath("$", hasSize(31)))
               .andExpect(jsonPath("$[0].id", is(1)))
               .andExpect(jsonPath("$[0].code", is("USD")))
               .andExpect(jsonPath("$[0].name", is("USA Dollar")))
               .andExpect(jsonPath("$[0].rateToEuro", greaterThan(.9)))
               .andReturn();
    }

    @SneakyThrows
    @Test
    void calculateExchangeResult() {
        mockMvc.perform(post("/rates/calculate/rate-amount")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(task)))
               .andExpect(jsonPath("$.from", is(1)))
               .andExpect(jsonPath("$.to", is(6)))
               .andExpect(jsonPath("$.amount", is(10)))
               .andExpect(jsonPath("$.result", lessThan(10.0)));
    }
}
