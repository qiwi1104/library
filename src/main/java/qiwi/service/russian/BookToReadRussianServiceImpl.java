package qiwi.service.russian;

import org.springframework.stereotype.Service;
import qiwi.model.russian.BookToReadRussian;
import qiwi.repository.russian.BookToReadRussianRepository;
import qiwi.service.common.BookToReadServiceImpl;

@Service
public class BookToReadRussianServiceImpl extends BookToReadServiceImpl<BookToReadRussian, BookToReadRussianRepository> {
}
