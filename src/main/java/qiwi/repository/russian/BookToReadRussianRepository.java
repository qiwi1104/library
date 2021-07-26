package qiwi.repository.russian;

import qiwi.model.russian.BookToReadRussian;
import qiwi.repository.common.BookToReadRepository;

import java.util.List;

public interface BookToReadRussianRepository extends BookToReadRepository<BookToReadRussian> {
    List<BookToReadRussian> findAllByOrderByIdAsc();
    List<BookToReadRussian> findAllByOrderByIdDesc();

    List<BookToReadRussian> findAllByOrderByFoundAsc();
    List<BookToReadRussian> findAllByOrderByFoundDesc();
}
