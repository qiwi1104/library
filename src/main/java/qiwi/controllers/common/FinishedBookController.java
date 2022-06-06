package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import qiwi.model.AdditionalDate;
import qiwi.model.book.FinishedBook;
import qiwi.model.input.PathInput;
import qiwi.service.impl.FinishedBookServiceImpl;
import qiwi.util.JSONHandler;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;
import qiwi.util.enums.SortType;
import qiwi.validator.FinishedBookValidator;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static qiwi.util.enums.BookType.FINISHED;
import static qiwi.util.enums.SortBy.START;
import static qiwi.util.enums.SortType.ASC;
import static qiwi.util.enums.SortType.DESC;

public abstract class FinishedBookController extends BookController {
    @Autowired
    protected FinishedBookServiceImpl service;
    protected FinishedBookValidator validator = new FinishedBookValidator();

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

    protected void setUpView(Model model, Language language) {
        List<FinishedBook> books = service.findAllByOrderByIdAsc(language);

        // Sorts according to current settings
        books = sortList(books);

        model.addAttribute("books", books);
        model.addAttribute("additionalDates", getAllAdditionalDates(books));
    }
}
