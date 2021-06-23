package qiwi.service;

import java.util.List;

public interface BookService<T> {
    void addBook(T book);
    void deleteBook(Integer id);
    T getBookById(Integer id);
    List<T> findAll();
}
