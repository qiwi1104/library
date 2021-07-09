package qiwi.repository.english;

import org.springframework.data.jpa.repository.JpaRepository;
import qiwi.model.english.BookToRead;

import java.util.List;

public interface BookToReadRepository extends JpaRepository<BookToRead, Integer> {
    List<BookToRead> findAllByOrderByIdAsc();
    List<BookToRead> findAllByOrderByIdDesc();

    List<BookToRead> findAllByOrderByFoundAsc();
    List<BookToRead> findAllByOrderByFoundDesc();
}
