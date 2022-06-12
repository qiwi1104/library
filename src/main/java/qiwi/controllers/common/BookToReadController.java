package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import qiwi.input.PathInput;
import qiwi.model.book.BookToRead;
import qiwi.service.impl.BookToReadServiceImpl;
import qiwi.service.impl.FinishedBookServiceImpl;
import qiwi.util.JSONHandler;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;
import qiwi.util.enums.SortType;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static qiwi.util.enums.BookType.TO_READ;
import static qiwi.util.enums.SortBy.FOUND;
import static qiwi.util.enums.SortType.ASC;
import static qiwi.util.enums.SortType.DESC;

public abstract class BookToReadController extends BookController {
    @Autowired
    protected BookToReadServiceImpl service;
    @Autowired
    protected FinishedBookServiceImpl finishedBookService;

    protected SortType sortDateMethod = ASC;
    protected SortBy sortProperty = FOUND;

    protected List<BookToRead> sortList(List<BookToRead> list) {
        switch (sortDateMethod) {
            case ASC:
                switch (sortProperty) {
                    case ID:
                        list.sort(Comparator.comparing(BookToRead::getId));
                        break;
                    case FOUND:
                        list.sort(Comparator.comparing(BookToRead::getFound).thenComparing(BookToRead::getId));
                        break;
                }
                break;
            case DESC:
                switch (sortProperty) {
                    case ID:
                        list.sort(Comparator.comparing(BookToRead::getId));
                        Collections.reverse(list);
                        break;
                    case FOUND:
                        list.sort(Comparator.comparing(BookToRead::getFound).thenComparing(BookToRead::getId));
                        Collections.reverse(list);
                        break;
                }
                break;
        }
        return list;
    }

    protected String add(BookToRead book, BindingResult result, Model model, Language language) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return "books-to-read/" + language.toLowerCase() + "/add-book";
        }

        if (service.exists(book)) {
            setUpView(model, language);

            result.reject("alreadyExists", "This book already exists.");

            return "books-to-read/" + language.toLowerCase() + "/add-book";
        }

        service.addBook(book);

        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    protected String edit(BookToRead book, BindingResult result, Model model, Language language) {
        validator.validate(book, result);

        if (result.hasErrors()) {
            setUpView(model, language);

            return "books-to-read/" + language.toLowerCase() + "/edit-book";
        }

        if (service.exists(book)) {
            setUpView(model, language);

            result.reject("alreadyExists", "This book already exists.");

            return "books-to-read/" + language.toLowerCase() + "/edit-book";
        }

        service.addBook(book);

        return "redirect:/bookstoread/" + language.toLowerCase() + "/";
    }

    protected void sort(SortBy sortProperty) {
        sortDateMethod = sortDateMethod.equals(ASC) ? DESC : ASC;
        this.sortProperty = sortProperty;
    }

    protected void load(PathInput input, Language language) {
        List<BookToRead> books = JSONHandler.IO.readJSONFile(input.getPath(), TO_READ, language);

        if (books.size() != 0) {
            service.clearLanguage(language);
            service.addAll(books);
        }
    }

    protected void loadBatch(PathInput input, Language language) {
        List<BookToRead> books = JSONHandler.IO.readJSONFile(input.getPath(), TO_READ, language);

        if (books.size() != 0) {
            service.addAll(books);
        }
    }

    protected void save(PathInput input, Language language) {
        List<BookToRead> bookToReadList = service.findAllByOrderByIdAsc(language);
        JSONHandler.IO.saveTableToJSON(bookToReadList, input.getPath(), language, TO_READ);
    }

    protected void setUpView(Model model, Language language) {
        List<BookToRead> books = service.findAllByOrderByIdAsc(language);

        books = sortList(books);

        model.addAttribute("booksToRead", books);
    }
}
