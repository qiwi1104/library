package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import qiwi.model.AdditionalDates;
import qiwi.model.book.FinishedBook;
import qiwi.model.input.Input;
import qiwi.model.input.PathInput;
import qiwi.service.impl.FinishedBookServiceImpl;
import qiwi.util.enums.Action;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;
import qiwi.util.enums.SortType;

import java.sql.Date;
import java.util.*;
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
    private List<AdditionalDates> getAllAdditionalDates(List<FinishedBook> books) {
        return books
                .stream()
                .filter(b -> b.getAdditionalDates().size() != 0)
                .map(FinishedBook::getAdditionalDates)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    protected List<FinishedBook> sortList(List<FinishedBook> list) {
        switch (sortDateMethod) {
            case ASC:
                switch (sortProperty) {
                    case ID:
                        list.sort(Comparator.comparing(FinishedBook::getId));
                        break;
                    case START:
                        list.sort(Comparator.comparing(FinishedBook::getStart).thenComparing(FinishedBook::getId));
                        break;
                    case END:
                        list.sort(Comparator.comparing(FinishedBook::getEnd).thenComparing(FinishedBook::getId));
                        break;
                    case FOUND:
                        list.sort(Comparator.comparing(FinishedBook::getFound).thenComparing(FinishedBook::getId));
                        break;
                }
                break;
            case DESC:
                switch (sortProperty) {
                    case ID:
                        list.sort(Comparator.comparing(FinishedBook::getId));
                        Collections.reverse(list);
                        break;
                    case START:
                        list.sort(Comparator.comparing(FinishedBook::getStart).thenComparing(FinishedBook::getId));
                        Collections.reverse(list);
                        break;
                    case END:
                        list.sort(Comparator.comparing(FinishedBook::getEnd).thenComparing(FinishedBook::getId));
                        Collections.reverse(list);
                        break;
                    case FOUND:
                        list.sort(Comparator.comparing(FinishedBook::getFound).thenComparing(FinishedBook::getId));
                        Collections.reverse(list);
                        break;
                }
                break;
        }
        return list;
    }

    protected String getRedirectionAddress(Input input, Model model, Language language, Action action) {
        String redirectTo = "redirect:/finishedbooks/" + language.toLowerCase() + "/";

        FinishedBook book = new FinishedBook();
        AdditionalDates additionalDates = new AdditionalDates();

        switch (action) {
            case ADD:
                setBookAttributesFromInput(book, input, ADD, language);

                if (input.getStart().equals(Date.valueOf("1970-01-01"))
                        || input.getEnd().equals(Date.valueOf("1970-01-01"))) {
                    model.addAttribute("emptyDatesMessage", "");
                    return showTable(model, language);
                }

                if (service.exists(book)) {
                    book = service.get(book);

                    additionalDates.setFinishedBookId(book.getId());
                    additionalDates.setStart(input.getStart());
                    additionalDates.setEnd(input.getEnd());

                    if (!book.hasDates(additionalDates)) {
                        service.addDates(additionalDates);
                        return redirectTo;
                    } else {
                        model.addAttribute("alreadyExistsMessage", "");
                        return showTable(model, language);
                    }

                } else {
                    if (input.getFound().equals(Date.valueOf("1970-01-01"))) {
                        model.addAttribute("emptyDatesMessage", "");
                        return showTable(model, language);
                    } else {
                        service.addBook(book);
                        return redirectTo;
                    }
                }
            case EDIT:
                if (input.getId() != null) {
                    book = service.getBookById(input.getId());
                    if (book.getLanguage().equals(language.firstLetterToUpperCase())) {
                        setBookAttributesFromInput(book, input, EDIT, language);
                    } else {
                        model.addAttribute("nonExistentMessageEdit", "");
                        return showTable(model, language);
                    }

                    return edit(input, model, language) ? redirectTo : showTable(model, language);
                } else {
                    model.addAttribute("nonExistentMessageEdit", "");
                    return showTable(model, language);
                }
        }

        return showTable(model, language);
    }

    protected boolean add(Input input, Model model, Language language) {
        FinishedBook book = new FinishedBook();
        AdditionalDates additionalDates = new AdditionalDates();

        setBookAttributesFromInput(book, input, ADD, language);

        if (service.exists(book)) {
            book = service.get(book);

            additionalDates.setFinishedBookId(book.getId());
            additionalDates.setStart(input.getStart());
            additionalDates.setEnd(input.getEnd());

            if (!book.hasDates(additionalDates)) {
                service.addDates(additionalDates);
                return true;
            }

            model.addAttribute("alreadyExistsMessage", "");
            return false;
        } else {
            service.addBook(book);
            return true;
        }
    }

    protected boolean edit(Input input, Model model, Language language) {
        FinishedBook book = service.getBookById(input.getId());

        if (book != null) {
            setBookAttributesFromInput(book, input, EDIT, language);
            service.addBook(book);
            return true;
        } else {
            model.addAttribute("nonExistentMessageEdit", "");
            return false;
        }
    }

    protected void sort(SortBy sortProperty) {
        sortDateMethod = sortDateMethod.equals(ASC) ? DESC : ASC;
        this.sortProperty = sortProperty;
    }

    protected void load(PathInput input, Language language) {
        List<FinishedBook> finishedBooks = JSONHandler.IO.readJSONFile(input.getPath(), FINISHED, language);

        if (finishedBooks.size() != 0) {
            service.clearLanguage(language);

            Map<FinishedBook, AdditionalDates> additionalDates = new HashMap<>();

            for (FinishedBook book : finishedBooks) {
                if (book.getAdditionalDates().size() != 0) {
                    book.getAdditionalDates().forEach(d -> additionalDates.put(book, d));
                    book.setAdditionalDates(new ArrayList<>());
                }
                book.setId(null);
            }

            service.addAll(finishedBooks);
            additionalDates.forEach((book, date) -> {
                date.setFinishedBookId(book.getId());
                service.addDates(date);
            });
        }
    }

    protected void save(PathInput input, Language language) {
        List<FinishedBook> books = service.findAllByOrderByIdAsc(language);
        JSONHandler.IO.saveTableToJSON(books, input.getPath(), language, FINISHED);
    }

    protected String showTable(Model model, Language language) {
        List<FinishedBook> books = service.findAllByOrderByIdAsc(language);

        // Sorts according to current settings
        books = sortList(books);

        model.addAttribute("books", books);
        model.addAttribute("additionalDates", getAllAdditionalDates(books));
        model.addAttribute("finished" + language.firstLetterToUpperCase() + "Input", new Input());

        return "finishedBooks" + language.firstLetterToUpperCase();
    }
}
