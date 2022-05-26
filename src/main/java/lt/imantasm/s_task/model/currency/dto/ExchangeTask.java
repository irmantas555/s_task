package lt.imantasm.s_task.model.currency.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeTask {
    private Long from;
    private Long to;
    private Long amount;
    private BigDecimal result;
}
