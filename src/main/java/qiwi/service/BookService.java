package qiwi.service;

import qiwi.model.common.book.Book;

import java.util.List;

public interface BookService<T extends Book> {
    void addBook(T book);

    void addAll(List<T> booksList);

    void deleteBook(Integer id);

    void clearAll();

    T getBookById(Integer id);

    List<T> findAll();
}
