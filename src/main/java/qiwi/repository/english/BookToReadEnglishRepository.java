package qiwi.repository.english;

import qiwi.model.english.BookToReadEnglish;
import qiwi.repository.common.BookToReadRepository;

import java.util.List;

public interface BookToReadEnglishRepository extends BookToReadRepository<BookToReadEnglish> {
    List<BookToReadEnglish> findAllByOrderByIdAsc();
    List<BookToReadEnglish> findAllByOrderByIdDesc();

    List<BookToReadEnglish> findAllByOrderByFoundAsc();
    List<BookToReadEnglish> findAllByOrderByFoundDesc();
}
