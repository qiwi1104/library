package qiwi.service.english;

import org.springframework.stereotype.Service;
import qiwi.model.english.BookToReadEnglish;
import qiwi.repository.english.BookToReadEnglishRepository;
import qiwi.service.common.BookToReadServiceImpl;

@Service
public class BookToReadEnglishServiceImpl extends BookToReadServiceImpl<BookToReadEnglish, BookToReadEnglishRepository> {
}
