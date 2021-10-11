package qiwi.controllers.common;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import qiwi.controllers.enums.BookType;
import qiwi.controllers.enums.Language;
import qiwi.controllers.enums.SortBy;
import qiwi.controllers.enums.SortType;
import qiwi.model.common.Input;
import qiwi.model.common.book.BookToRead;
import qiwi.model.common.book.FinishedBook;
import qiwi.model.english.BookToReadEnglish;
import qiwi.model.russian.BookToReadRussian;
import qiwi.model.spanish.BookToReadSpanish;
import qiwi.repository.common.BookToReadRepository;
import qiwi.repository.common.FinishedBookRepository;
import qiwi.service.common.BookToReadServiceImpl;
import qiwi.service.common.FinishedBookServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static qiwi.controllers.common.BookController.JSONHandler.Conversion.setAttributes;
import static qiwi.controllers.enums.Context.*;
import static qiwi.controllers.enums.SortBy.*;
import static qiwi.controllers.enums.SortType.*;

public abstract class BookToReadController<
        T extends BookToRead,
        S extends BookToReadServiceImpl<T, ? extends BookToReadRepository<T>>,
        U extends FinishedBook,
        V extends FinishedBookServiceImpl<U, ? extends FinishedBookRepository<U>>> extends BookController {
    @Autowired
    protected S service;
    @Autowired
    protected V finishedBookService;

    protected SortType sortDateMethod = ASC;
    protected SortBy sortProperty = FOUND;

    private List<T> fillList(JSONArray source, Language language) {
        List<T> destination = new ArrayList<>();
        int librarySize = service.findAll().size();

        for (int i = librarySize; i < source.length() + librarySize; i++) {
            T bookToAdd;

            switch (language) {
                case ENGLISH:
                    bookToAdd = (T) new BookToReadEnglish();
                    break;
                case RUSSIAN:
                    bookToAdd = (T) new BookToReadRussian();
                    break;
                case SPANISH:
                    bookToAdd = (T) new BookToReadSpanish();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + language);
            }

            setAttributes(bookToAdd, source.getJSONObject(i - librarySize), i + 1);

            destination.add(bookToAdd);
        }

        return destination;
    }

    @Override
    protected List<T> filterAndSort() {
        List<T> books = null;

        switch (sortDateMethod) {
            case ASC:
                switch (sortProperty) {
                    case ID:
                        books = service.findAllByOrderByIdAsc();
                        break;
                    case FOUND:
                        books = service.findAllByOrderByFoundByIdAsc();
                        break;
                }
                break;
            case DESC:
                switch (sortProperty) {
                    case ID:
                        books = service.findAllByOrderByIdDesc();
                        break;
                    case FOUND:
                        books = service.findAllByOrderByFoundByIdDesc();
                        break;
                }
                break;
        }
        return books;
    }

    protected void add(Input input, T book) {
        book.setId(service.findAll().size() + 1);
        setBookAttributesFromInput(book, input, ADD);

        if (!service.exists(book)) {
            service.addBook(book);
        } else {
            System.out.println("This book already exists!");
        }
    }

    protected void edit(Input input) {
        T book = service.getBookById(input.getId());
        setBookAttributesFromInput(book, input, EDIT);
        service.addBook(book);
    }

    protected void finish(Input input, U finishedBook) {
        T bookToRead = service.getBookById(input.getId());

        finishedBook.setId(finishedBookService.findAll().size() + 1);
        finishedBook.setAuthor(bookToRead.getAuthor());
        finishedBook.setName(bookToRead.getName());
        finishedBook.setStart(input.getStart());
        finishedBook.setEnd(input.getEnd());
        finishedBook.setFound(bookToRead.getFound());
        finishedBook.setDescription(bookToRead.getDescription());

        finishedBookService.addBook(finishedBook);
        service.deleteBook(bookToRead.getId());
    }

    protected void sort(SortBy sortProperty) {
        sortDateMethod = sortDateMethod.equals(ASC) ? DESC : ASC;
        this.sortProperty = sortProperty;
    }

    protected void load(Input input, Language language) {
        JSONArray jsonBooks = JSONHandler.IO.readJSONFile(input.getName(), BookType.TO_READ, language);
        if (jsonBooks.length() != 0) {
            service.clearAll();

            List<T> booksToRead = fillList(jsonBooks, language);

            service.addAll(booksToRead);
        } else {
            System.out.println("The list is empty :(");
        }
    }

    protected void loadBatch(Input input, Language language) {
        JSONArray jsonBooks = JSONHandler.IO.readJSONFile(input.getName());
        if (jsonBooks.length() != 0) {
            List<T> bookToReadList = fillList(jsonBooks, language);
            service.addAll(bookToReadList);
        } else {
            System.out.println("The list is empty :(");
        }
    }

    protected void save(Input input, Language language) {
        List<T> bookToReadList = service.findAll();
        JSONHandler.IO.saveTableToJSON(bookToReadList, input.getName(), language);
    }

    protected void list(Model model, List<T> bookList) {
        bookList = filterAndSort();

        model.addAttribute("booksToRead", bookList);
    }
}
