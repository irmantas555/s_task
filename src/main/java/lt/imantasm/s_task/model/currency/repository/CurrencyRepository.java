package lt.imantasm.s_task.model.currency.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import lt.imantasm.s_task.model.currency.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    List<Currency> findAllByDateOrderByName(LocalDate date);

    @Query(value = "SELECT DISTINCT date FROM Currency")
    List<LocalDate> findAvailableDates();
}
