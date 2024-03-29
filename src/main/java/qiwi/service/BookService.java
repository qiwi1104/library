package qiwi.service;

import org.springframework.data.domain.Sort;
import qiwi.model.book.Book;
import qiwi.model.enums.Language;

import java.util.List;

public interface BookService<T extends Book> {
    void addBook(T book);

    void addAll(List<T> booksList);

    void deleteBookById(Integer id);

    void clearLanguage(Language language);

    T getBookById(Integer id);

    boolean exists(T book);

    List<T> findAll();

    List<T> findAll(Sort sort);
}
