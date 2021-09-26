package qiwi.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import qiwi.model.common.book.BookToRead;

public interface BookToReadRepository<T extends BookToRead> extends JpaRepository<T, Integer> {
    @Procedure
    void computeIds();
}
