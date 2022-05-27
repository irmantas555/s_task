package lt.imantasm.s_task.model.currency.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import lt.imantasm.s_task.model.currency.entity.Currency;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class CurrencyRepositoryTest {

    @Autowired
    CurrencyRepository currencyRepository;

    Currency eu;

    @BeforeEach
    public void setup() throws Exception {
        eu = new Currency(null, "EUR", "European Euro", BigDecimal.valueOf(1.0), LocalDate.now());
    }

    @Test
    @DisplayName("Test repository save if successful")
    void saveTest() {
        Currency euro = currencyRepository.save(eu);
        Optional<Currency> savedEuro = currencyRepository.findById(euro.getId());
        assertNotNull(savedEuro);
    }

    @Test
    @DisplayName("Test repository save and retrieve if successful")
    void finAllTest() {
        Currency euro = currencyRepository.save(eu);
        List<Currency> all = currencyRepository.findAll();
        assertEquals(1, all.size());
    }

}
