package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import qiwi.model.book.BookToRead;
import qiwi.model.book.FinishedBook;
import qiwi.model.input.Input;
import qiwi.model.input.PathInput;
import qiwi.service.impl.BookToReadServiceImpl;
import qiwi.service.impl.FinishedBookServiceImpl;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;
import qiwi.util.enums.SortType;

import java.sql.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static qiwi.util.enums.Action.ADD;
import static qiwi.util.enums.Action.EDIT;
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

    protected String add(Input input, Model model, Language language) {
        String redirectTo = "redirect:/bookstoread/" + language.toLowerCase() + "/";
        String viewName = "booksToRead" + language.firstLetterToUpperCase();

        BookToRead book = new BookToRead();
        setBookAttributesFromInput(book, input, ADD, language);

        if (!service.exists(book)) {
            if (!input.getFound().equals(Date.valueOf("1970-01-01"))) {
                service.addBook(book);
                return redirectTo;
            } else {
                model.addAttribute("emptyDatesMessage", "");
                setUpView(model, language);
                return viewName;
            }
        } else {
            model.addAttribute("alreadyExistsMessage", "");
            setUpView(model, language);
            return viewName;
        }
    }

    protected String edit(Input input, Model model, Language language) {
        String redirectTo = "redirect:/bookstoread/" + language.toLowerCase() + "/";
        String viewName = "booksToRead" + language.firstLetterToUpperCase();

        if (input.getId() != null) {
            BookToRead book = service.getBookById(input.getId());

            if (book != null) {
                if (book.getLanguage().equals(language.firstLetterToUpperCase())) {
                    setBookAttributesFromInput(book, input, EDIT, language);
                    service.addBook(book);
                    return redirectTo;
                } else {
                    model.addAttribute("nonExistentMessageEdit", "");
                    setUpView(model, language);
                    return viewName;
                }
            } else {
                model.addAttribute("nonExistentMessageEdit", "");
                setUpView(model, language);
                return viewName;
            }
        } else {
            model.addAttribute("nonExistentMessageEdit", "");
            setUpView(model, language);
            return viewName;
        }
    }

    protected String finish(Input input, Model model, Language language, boolean hasErrors) {
        if (!hasErrors) {
            BookToRead bookToRead = service.getBookById(input.getId());

            if (bookToRead != null) {
                FinishedBook finishedBook = new FinishedBook();

                finishedBook.setAuthor(bookToRead.getAuthor());
                finishedBook.setName(bookToRead.getName());
                finishedBook.setStart(input.getStart());
                finishedBook.setEnd(input.getEnd());
                finishedBook.setFound(bookToRead.getFound());
                finishedBook.setDescription(bookToRead.getDescription());
                finishedBook.setLanguage(bookToRead.getLanguage());

                finishedBookService.addBook(finishedBook);
                service.deleteBookById(bookToRead.getId());
                return "redirect:/bookstoread/" + language.toLowerCase() + "/";
            } else {
                model.addAttribute("nonExistentMessage", "");
                return "booksToRead" + language.firstLetterToUpperCase();
            }
        } else {
            setUpView(model, language);
            return "booksToRead" + language.firstLetterToUpperCase();
        }
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

        model.addAttribute("booksToRead" + language.firstLetterToUpperCase() + "Input", new Input());
        model.addAttribute("booksToRead", books);
    }
}
