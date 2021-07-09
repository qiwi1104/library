package qiwi.service;

import java.util.List;

public interface BookService<T> {
    void addBook(T book);

    void addAll(List<T> booksList);

    void deleteBook(Integer id);

    void clearAll();

    T getBookById(Integer id);

    List<T> findAll();
}
