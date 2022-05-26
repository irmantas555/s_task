package lt.imantasm.s_task.model.currency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.imantasm.s_task.model.currency.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
