package qiwi.repository.spanish;

import org.springframework.data.jpa.repository.query.Procedure;
import qiwi.model.spanish.BookToReadSpanish;
import qiwi.repository.common.BookToReadRepository;

public interface BookToReadSpanishRepository extends BookToReadRepository<BookToReadSpanish> {
    @Override
    @Procedure("computeIdsSpanish")
    void computeIds();
}
