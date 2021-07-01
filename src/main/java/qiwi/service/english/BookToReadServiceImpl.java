package qiwi.service.english;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qiwi.model.english.BookToRead;
import qiwi.model.english.FinishedBook;
import qiwi.repository.english.BookToReadRepository;
import qiwi.service.BookService;

import java.util.List;

@Service
public class BookToReadServiceImpl implements BookService<BookToRead> {
    @Autowired
    private BookToReadRepository bookToReadRepository;

    public List<BookToRead> findAllByOrderByIdAsc() {
        return bookToReadRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<BookToRead> findAllByOrderByIdDesc() {
        return bookToReadRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<BookToRead> findAllByOrderByFoundAsc() {
        return bookToReadRepository.findAll(Sort.by(Sort.Direction.ASC, "found"));
    }

    public List<BookToRead> findAllByOrderByFoundDesc() {
        return bookToReadRepository.findAll(Sort.by(Sort.Direction.DESC, "found"));
    }

    @Override
    public void addBook(BookToRead book) {
        bookToReadRepository.save(book);
    }

    @Override
    public void addAll(List<BookToRead> booksList) {
        bookToReadRepository.saveAll(booksList);
    }

    @Override
    public void deleteBook(Integer id) {
        bookToReadRepository.deleteById(id);
    }

    @Override
    public void clearAll() {
        bookToReadRepository.deleteAll();
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
