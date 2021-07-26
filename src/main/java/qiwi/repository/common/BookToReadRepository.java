package qiwi.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import qiwi.model.common.book.BookToRead;

public interface BookToReadRepository<T extends BookToRead> extends JpaRepository<T, Integer> {
}
