package qiwi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qiwi.model.book.BookToRead;

public interface BookToReadRepository extends JpaRepository<BookToRead, Integer> {
}
