package qiwi.repository.english;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Service;
import qiwi.model.english.FinishedBookEnglish;
import qiwi.repository.common.FinishedBookRepository;

@Service
public interface FinishedBookEnglishRepository extends FinishedBookRepository<FinishedBookEnglish> {
    @Override
    @Procedure("computeIdsFinishedEnglish")
    void computeIds();
}