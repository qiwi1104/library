package qiwi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qiwi.model.english.BookToRead;
import qiwi.repository.english.BookToReadRepository;

import java.util.List;

@Service
public class BookToReadEnglishSerivceImpl implements BookService<BookToRead> {
    @Autowired
    private BookToReadRepository bookRepository;

    @Override
    public void addBook(BookToRead book) {
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookToRead getBookById(Integer id) {
        return bookRepository.getOne(id);
    }

    @Override
    public List<BookToRead> findAll() {
        return bookRepository.findAll();
    }
}
