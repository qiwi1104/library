package qiwi.service.russian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qiwi.model.russian.BookToReadRussian;
import qiwi.repository.russian.BookToReadRussianRepository;
import qiwi.service.BookService;

import java.util.List;

@Service
public class BookToReadServiceRussianImpl implements BookService<BookToReadRussian> {
    @Autowired
    private BookToReadRussianRepository bookToReadRussianRepository;

    public List<BookToReadRussian> findAllByOrderByIdAsc() {
        return bookToReadRussianRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<BookToReadRussian> findAllByOrderByIdDesc() {
        return bookToReadRussianRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<BookToReadRussian> findAllByOrderByFoundAsc() {
        return bookToReadRussianRepository.findAll(Sort.by(Sort.Direction.ASC, "found"));
    }

    public List<BookToReadRussian> findAllByOrderByFoundDesc() {
        return bookToReadRussianRepository.findAll(Sort.by(Sort.Direction.DESC, "found"));
    }

    @Override
    public void addBook(BookToReadRussian book) {
        bookToReadRussianRepository.save(book);
    }

    @Override
    public void addAll(List<BookToReadRussian> booksList) {
        bookToReadRussianRepository.saveAll(booksList);
    }

    @Override
    public void deleteBook(Integer id) {
        bookToReadRussianRepository.deleteById(id);
    }

    @Override
    public void clearAll() {
        bookToReadRussianRepository.deleteAll();
    }

    @Override
    public BookToReadRussian getBookById(Integer id) {
        return bookToReadRussianRepository.getOne(id);
    }

    @Override
    public List<BookToReadRussian> findAll() {
        return bookToReadRussianRepository.findAll();
    }
}
