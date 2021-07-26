package qiwi.repository.spanish;

import qiwi.model.spanish.BookToReadSpanish;
import qiwi.repository.common.BookToReadRepository;

import java.util.List;

public interface BookToReadSpanishRepository extends BookToReadRepository<BookToReadSpanish> {
    List<BookToReadSpanish> findAllByOrderByIdAsc();
    List<BookToReadSpanish> findAllByOrderByIdDesc();

    List<BookToReadSpanish> findAllByOrderByFoundAsc();
    List<BookToReadSpanish> findAllByOrderByFoundDesc();
}
