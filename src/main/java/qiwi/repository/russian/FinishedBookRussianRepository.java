package qiwi.repository.russian;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Service;
import qiwi.model.russian.FinishedBookRussian;
import qiwi.repository.common.FinishedBookRepository;

@Service
public interface FinishedBookRussianRepository extends FinishedBookRepository<FinishedBookRussian> {
    @Override
    @Procedure("computeIdsFinishedRussian")
    void computeIds();
}