package qiwi.service.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qiwi.model.book.BookToRead;
import qiwi.model.enums.Language;
import qiwi.repository.BookToReadRepository;
import qiwi.service.BookService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookToReadDAO implements BookService<BookToRead> {
    @Autowired
    private BookToReadRepository repository;

    @Override
    public void addBook(BookToRead book) {
        repository.save(book);
    }

    @Override
    public void addAll(List<BookToRead> booksList) {
        repository.saveAll(booksList);
    }

    @Override
    public void deleteBookById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public void clearLanguage(Language language) {
        for (BookToRead book : repository.findAll()) {
            if (book.getLanguage().equals(language.firstLetterToUpperCase())) {
                if (repository.existsById(book.getId())) {
                    repository.deleteById(book.getId());
                }
            }
        }
    }

    @Override
    public BookToRead getBookById(Integer id) {
        Optional<BookToRead> book = repository.findById(id);
        return book.orElse(null);
    }

    @Override
    public boolean exists(BookToRead book) {
        return repository.findAll().stream()
                .filter(b -> b.getLanguage().equals(book.getLanguage()))
                .collect(Collectors.toList())
                .contains(book);
    }

    @Override
    public List<BookToRead> findAll() {
        return repository.findAll();
    }

    public List<BookToRead> findAll(Sort sort) {
        return repository.findAll(sort);
    }
}
