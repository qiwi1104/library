package qiwi.repository.english;

import org.springframework.data.jpa.repository.query.Procedure;
import qiwi.model.english.AdditionalDatesEnglish;
import qiwi.repository.common.AdditionalDatesRepository;

public interface AdditionalDatesEnglishRepository extends AdditionalDatesRepository<AdditionalDatesEnglish> {
    @Override
    @Procedure("computeIdsDatesEnglish")
    void computeIds();
}
