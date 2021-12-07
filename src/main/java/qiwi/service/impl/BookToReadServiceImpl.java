package qiwi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import qiwi.model.book.BookToRead;
import qiwi.repository.BookToReadRepository;
import qiwi.service.BookService;
import qiwi.util.enums.Language;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookToReadServiceImpl implements BookService<BookToRead> {
    @Autowired
    private BookToReadRepository repository;

    public List<BookToRead> findAllByOrderByIdAsc(Language language) {
        return repository
                .findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .filter(b -> b.getLanguage().equals(language.firstLetterToUpperCase()))
                .collect(Collectors.toList());
    }

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
    public void clearAll() {
        repository.deleteAll();
    }

    @Override
    public void clearLanguage(Language language) {
        for (BookToRead book : repository.findAll())
            if (book.getLanguage().equals(language.firstLetterToUpperCase()))
                if (repository.existsById(book.getId()))
                    repository.deleteById(book.getId());
    }

    @Override
    public BookToRead getBookById(Integer id) {
        return repository.getOne(id);
    }

    @Override
    public boolean exists(BookToRead book) {
        return repository.findAll().contains(book);
    }

    @Override
    public List<BookToRead> findAll() {
        return repository.findAll();
    }
}
