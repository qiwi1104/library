package qiwi.repository.english;

import org.springframework.data.jpa.repository.query.Procedure;
import qiwi.model.english.BookToReadEnglish;
import qiwi.repository.common.BookToReadRepository;

public interface BookToReadEnglishRepository extends BookToReadRepository<BookToReadEnglish> {
    @Override
    @Procedure("computeIdsEnglish")
    void computeIds();
}
