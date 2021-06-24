package qiwi.service.english;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.model.english.BookToRead;
import qiwi.repository.english.BookToReadRepository;
import qiwi.service.BookService;

import java.util.List;

@Service
public class BookToReadServiceImpl implements BookService<BookToRead> {
    @Autowired
    private BookToReadRepository bookToReadRepository;

    @Override
    public void addBook(BookToRead book) {
        bookToReadRepository.save(book);
    }

    @Override
    public void deleteBook(Integer id) {
        bookToReadRepository.deleteById(id);
    }

    @Override
    public BookToRead getBookById(Integer id) {
        return bookToReadRepository.getOne(id);
    }

    @Override
    public List<BookToRead> findAll() {
        return bookToReadRepository.findAll();
    }
}
