package qiwi.repository.english;

import org.springframework.data.jpa.repository.JpaRepository;
import qiwi.model.english.BookToRead;

public interface BookToReadRepository extends JpaRepository<BookToRead, Integer> {
}
