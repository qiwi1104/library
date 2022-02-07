package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import qiwi.model.AdditionalDate;
import qiwi.model.book.FinishedBook;
import qiwi.model.input.Input;
import qiwi.model.input.PathInput;
import qiwi.service.impl.FinishedBookServiceImpl;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;
import qiwi.util.enums.SortType;

import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static qiwi.util.enums.Action.ADD;
import static qiwi.util.enums.Action.EDIT;
import static qiwi.util.enums.BookType.FINISHED;
import static qiwi.util.enums.SortBy.START;
import static qiwi.util.enums.SortType.ASC;
import static qiwi.util.enums.SortType.DESC;

public abstract class FinishedBookController extends BookController {
    @Autowired
    protected FinishedBookServiceImpl service;

    protected SortType sortDateMethod = ASC;
    protected SortBy sortProperty = START;

    /*
     * Returns all the additional dates that books contain
     * */
    private List<AdditionalDate> getAllAdditionalDates(List<FinishedBook> books) {
        return books
                .stream()
                .filter(b -> b.getAdditionalDates().size() != 0)
                .map(FinishedBook::getAdditionalDates)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    protected List<FinishedBook> sortList(List<FinishedBook> books) {
        switch (sortDateMethod) {
            case ASC:
                switch (sortProperty) {
                    case ID:
                        books.sort(Comparator.comparing(FinishedBook::getId));
                        break;
                    case START:
                        books.sort(Comparator.comparing(FinishedBook::getStart).thenComparing(FinishedBook::getId));
                        break;
                    case END:
                        books.sort(Comparator.comparing(FinishedBook::getEnd).thenComparing(FinishedBook::getId));
                        break;
                    case FOUND:
                        books.sort(Comparator.comparing(FinishedBook::getFound).thenComparing(FinishedBook::getId));
                        break;
                }
                break;
            case DESC:
                switch (sortProperty) {
                    case ID:
                        books.sort(Comparator.comparing(FinishedBook::getId));
                        Collections.reverse(books);
                        break;
                    case START:
                        books.sort(Comparator.comparing(FinishedBook::getStart).thenComparing(FinishedBook::getId));
                        Collections.reverse(books);
                        break;
                    case END:
                        books.sort(Comparator.comparing(FinishedBook::getEnd).thenComparing(FinishedBook::getId));
                        Collections.reverse(books);
                        break;
                    case FOUND:
                        books.sort(Comparator.comparing(FinishedBook::getFound).thenComparing(FinishedBook::getId));
                        Collections.reverse(books);
                        break;
                }
                break;
        }
        return books;
    }

    protected String add(Input input, Model model, Language language) {
        String redirectTo = "redirect:/finishedbooks/" + language.toLowerCase() + "/";
        String viewName = "finishedBooks" + language.firstLetterToUpperCase();

        FinishedBook book = new FinishedBook();
        AdditionalDate additionalDate = new AdditionalDate();

        setBookAttributesFromInput(book, input, ADD, language);

        if (input.getStart().equals(Date.valueOf("1970-01-01"))
                || input.getEnd().equals(Date.valueOf("1970-01-01"))) {
            model.addAttribute("emptyDatesMessage", "");
            setUpView(model, language, input);
            return viewName;
        }

        if (service.exists(book)) {
            book = service.get(book);

            additionalDate.setStart(input.getStart());
            additionalDate.setEnd(input.getEnd());

            if (!book.hasDate(additionalDate)) {
                book.addDate(additionalDate);
                service.addBook(book);
                return redirectTo;
            } else {
                model.addAttribute("alreadyExistsMessage", "");
                setUpView(model, language, input);
                return viewName;
            }

        } else {
            if (input.getFound().equals(Date.valueOf("1970-01-01"))) {
                model.addAttribute("emptyDatesMessage", "");
                setUpView(model, language, input);
                return viewName;
            } else {
                service.addBook(book);
                return redirectTo;
            }
        }
    }

    protected String edit(Input input, Model model, Language language) {
        String redirectTo = "redirect:/finishedbooks/" + language.toLowerCase() + "/";
        String viewName = "finishedBooks" + language.firstLetterToUpperCase();

        if (input.getId() != null) {
            FinishedBook book = service.getBookById(input.getId());

            if (book != null) {
                if (book.getLanguage().equals(language.firstLetterToUpperCase())) {
                    setBookAttributesFromInput(book, input, EDIT, language);
                    service.addBook(book);
                    return redirectTo;
                }
            }
        }

        model.addAttribute("nonExistentMessageEdit", "");
        setUpView(model, language, input);
        return viewName;
    }

    protected void sort(SortBy sortProperty) {
        sortDateMethod = sortDateMethod.equals(ASC) ? DESC : ASC;
        this.sortProperty = sortProperty;
    }

    protected void load(PathInput input, Language language) {
        List<FinishedBook> books = JSONHandler.IO.readJSONFile(input.getPath(), FINISHED, language);

        if (books.size() != 0) {
            service.clearLanguage(language);
            service.addAll(books);
        }
    }

    protected void save(PathInput input, Language language) {
        List<FinishedBook> books = service.findAllByOrderByIdAsc(language);
        JSONHandler.IO.saveTableToJSON(books, input.getPath(), language, FINISHED);
    }

    protected void setUpView(Model model, Language language, Input input) {
        List<FinishedBook> books = service.findAllByOrderByIdAsc(language);

        // Sorts according to current settings
        books = sortList(books);

        model.addAttribute("books", books);
        model.addAttribute("additionalDates", getAllAdditionalDates(books));
        model.addAttribute("finished" + language.firstLetterToUpperCase() + "Input", input);
    }
}
