package lt.imantasm.s_task;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lt.imantasm.s_task.controller.RatesController;
import lt.imantasm.s_task.model.currency.service.CurrencyRatesService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class STaskApplicationTests {

    @Autowired
    RatesController ratesController;

    @Test
    void contextLoads() {
        assertThat(ratesController).isNotNull();
    }

}
