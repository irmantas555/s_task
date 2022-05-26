package lt.imantasm.s_task.model.currency.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ExchangeTask {
    private Long from;
    private Long to;
    private Long amount;
    private BigDecimal result;
}
