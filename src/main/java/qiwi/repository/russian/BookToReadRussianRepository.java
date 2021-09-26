package qiwi.repository.russian;

import org.springframework.data.jpa.repository.query.Procedure;
import qiwi.model.russian.BookToReadRussian;
import qiwi.repository.common.BookToReadRepository;

public interface BookToReadRussianRepository extends BookToReadRepository<BookToReadRussian> {
    @Override
    @Procedure("computeIdsRussian")
    void computeIds();
}
