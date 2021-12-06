package qiwi.repository.spanish;

import org.springframework.data.jpa.repository.query.Procedure;
import qiwi.model.spanish.AdditionalDatesSpanish;
import qiwi.repository.common.AdditionalDatesRepository;

public interface AdditionalDatesSpanishRepository extends AdditionalDatesRepository<AdditionalDatesSpanish> {
    @Override
    @Procedure("computeIdsDatesSpanish")
    void computeIds();
}
