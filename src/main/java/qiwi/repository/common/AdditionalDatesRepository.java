package qiwi.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import qiwi.model.common.AdditionalDates;

public interface AdditionalDatesRepository<T extends AdditionalDates> extends JpaRepository<T, Integer> {
    @Procedure
    void computeIds();
}
