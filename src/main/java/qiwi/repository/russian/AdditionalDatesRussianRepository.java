package qiwi.repository.russian;

import org.springframework.data.jpa.repository.query.Procedure;
import qiwi.model.russian.AdditionalDatesRussian;
import qiwi.repository.common.AdditionalDatesRepository;

public interface AdditionalDatesRussianRepository extends AdditionalDatesRepository<AdditionalDatesRussian> {
    @Override
    @Procedure("computeIdsDatesRussian")
    void computeIds();
}
