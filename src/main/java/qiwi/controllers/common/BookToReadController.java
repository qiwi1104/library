package qiwi.controllers.common;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import qiwi.IO;
import qiwi.TimeFormat;
import qiwi.controllers.enums.Language;
import qiwi.controllers.enums.SortBy;
import qiwi.controllers.enums.SortType;
import qiwi.model.common.Input;
import qiwi.model.common.book.BookToRead;
import qiwi.model.common.book.FinishedBook;
import qiwi.model.english.BookToReadEnglish;
import qiwi.model.russian.BookToReadRussian;
import qiwi.model.spanish.BookToReadSpanish;
import qiwi.repository.common.FinishedBookRepository;
import qiwi.service.common.BookToReadServiceImpl;
import qiwi.service.common.FinishedBookServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static qiwi.controllers.enums.Context.*;
import static qiwi.controllers.enums.SortBy.FOUND;
import static qiwi.controllers.enums.SortType.ASC;
import static qiwi.controllers.enums.SortType.DESC;

public abstract class BookToReadController<
        T extends BookToRead,
        S extends BookToReadServiceImpl<T, ?>,
        U extends FinishedBook,
        V extends FinishedBookServiceImpl<U, ? extends FinishedBookRepository<U>>> extends BookController {
    @Autowired
    protected S service;
    @Autowired
    protected V finishedBookService;

    protected SortType sortDateMethod = ASC;
    protected SortBy sortProperty = FOUND;

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

    private List<T> fillList(JSONArray source, Language language) {
        List<T> destination = new ArrayList<>();

        for (int i = 0; i < source.length(); i++) {
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

            bookToAdd.setId(i + 1);
            bookToAdd.setAuthor(source.getJSONObject(i).get("author").toString());
            bookToAdd.setName(source.getJSONObject(i).get("name").toString());

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d", source.getJSONObject(i).get("found").toString());

                bookToAdd.setFound(java.sql.Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setFound(java.sql.Date.valueOf("1970-1-1"));
            }

            bookToAdd.setDescription(source.getJSONObject(i).get("description").toString());

            destination.add(bookToAdd);
        }

        return destination;
    }

    protected void add(Input input, T book) {
        book.setId(service.findAll().size() + 1);
        setBookAttributesFromInput(book, input, ADD_FIRST);
        setBookAttributesFromInput(book, input, ADD_SECOND);

        service.addBook(book);
    }

    protected void edit(Input input, T book) {
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

    protected void load(Input input, Language language) throws IOException {
        service.clearAll();
        List<T> bookToReadList;

        JSONArray jsonArray = IO.readJSONFile(input.getName());
        bookToReadList = fillList(jsonArray, language);

        service.addAll(bookToReadList);
    }

    protected void save(Input input, Language language) {
        List<T> bookToReadList = service.findAll();

        saveTableToJSON(bookToReadList, input.getName(), language);
    }

    protected void list(Model model, List<T> bookList) {
        bookList = filterAndSort();

        model.addAttribute("booksToRead", bookList);
    }
}
