package qiwi.repository.english;

import org.springframework.data.jpa.repository.JpaRepository;
import qiwi.model.english.BookToReadEnglish;

public interface BookToReadRepository extends JpaRepository<BookToReadEnglish, Integer> {
}
