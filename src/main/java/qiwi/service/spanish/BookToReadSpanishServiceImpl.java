package qiwi.service.spanish;

import org.springframework.stereotype.Service;
import qiwi.model.spanish.BookToReadSpanish;
import qiwi.repository.spanish.BookToReadSpanishRepository;
import qiwi.service.common.BookToReadServiceImpl;

@Service
public class BookToReadSpanishServiceImpl extends BookToReadServiceImpl<BookToReadSpanish, BookToReadSpanishRepository> {
}
