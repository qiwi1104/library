package qiwi.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;
import qiwi.model.common.book.FinishedBook;

public interface FinishedBookRepository<T extends FinishedBook> extends JpaRepository<T, Integer> {
}
