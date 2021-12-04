package qiwi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import qiwi.model.AdditionalDates;

public interface AdditionalDatesRepository extends JpaRepository<AdditionalDates, Integer> {
    @Procedure("computeIdsDates")
    void computeIds();
}
