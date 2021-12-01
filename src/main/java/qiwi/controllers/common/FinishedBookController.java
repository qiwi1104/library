package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import qiwi.model.common.AdditionalDates;
import qiwi.model.common.book.FinishedBook;
import qiwi.model.common.input.Input;
import qiwi.model.common.input.PathInput;
import qiwi.repository.common.AdditionalDatesRepository;
import qiwi.repository.common.FinishedBookRepository;
import qiwi.service.common.AdditionalDatesServiceImpl;
import qiwi.service.common.FinishedBookServiceImpl;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;
import qiwi.util.enums.SortType;

import java.util.List;

import static qiwi.util.enums.BookType.FINISHED;
import static qiwi.util.enums.Context.ADD;
import static qiwi.util.enums.Context.EDIT;
import static qiwi.util.enums.SortBy.START;
import static qiwi.util.enums.SortType.ASC;
import static qiwi.util.enums.SortType.DESC;

public abstract class FinishedBookController<
        T extends FinishedBook,
        S extends FinishedBookServiceImpl<T, ? extends FinishedBookRepository<T>>,
        U extends AdditionalDates,
        V extends AdditionalDatesServiceImpl<U, ? extends AdditionalDatesRepository<U>>> extends BookController {
    @Autowired
    protected S service;
    @Autowired
    protected V additionalDatesService;

    protected SortType sortDateMethod = ASC;
    protected SortBy sortProperty = START;

    /*
     * Sets ids to Additional Dates of a book
     * */
    private void setIdsAndDates(List<T> books) {
        int additionalDatesCounter = 0;

        for (T book : books) {
            List<U> additionalDatesList = (List<U>) book.getAdditionalDates();

            if (additionalDatesList.size() != 0) {
                for (int j = 0; j < additionalDatesList.size(); j++) {
                    additionalDatesCounter++;

                    U additionalDates = additionalDatesList.get(j);
                    additionalDates.setFinishedBookId(book.getId());
                    additionalDates.setId(additionalDatesCounter);
                }
            }
        }
    }

    /*
     * Returns either a redirection link to the respective page (if there are no errors)
     * Or a view name (if there are errors)
     * */
    protected String getRedirectionAddress(Input input, BindingResult result, Model model, Language language, T book, U additionalDates) {
        if (result.hasErrors())
            return showTable(input, model, language);

        if (add(input, model, book, additionalDates))
            return "redirect:/finishedbooks/" + language.toLowerCase() + "/";
        else return showTable(input, model, language);
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
                    case START:
                        books = service.findAllByOrderByStartAsc();
                        break;
                    case END:
                        books = service.findAllByOrderByEndAsc();
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
                    case START:
                        books = service.findAllByOrderByStartDesc();
                        break;
                    case END:
                        books = service.findAllByOrderByEndDesc();
                        break;
                    case FOUND:
                        books = service.findAllByOrderByFoundByIdDesc();
                        break;
                }
                break;
        }
        return books;
    }

    protected boolean add(Input input, Model model, T book, U additionalDates) {
        book.setId(service.findAll().size() + 1);
        setBookAttributesFromInput(book, input, ADD);

        if (service.exists(book)) {
            book.setId(service.get(book).getId());
            book.setFound(service.get(book).getFound());

            additionalDates.setId(additionalDatesService.findAll().size() + 1);
            additionalDates.setFinishedBookId(book.getId());
            additionalDates.setStart(book.getStart());
            additionalDates.setEnd(book.getEnd());

            if (!additionalDatesService.exists(additionalDates)) {
                additionalDatesService.addDates(additionalDates);
                return true;
            }

            model.addAttribute("alreadyExistsMessage", "");
            return false;
        } else {
            service.addBook(book);
            return true;
        }
    }

    @Override
    protected boolean edit(Input input, Model model) {
        T book = service.getBookById(input.getId());

        if (book != null) {
            setBookAttributesFromInput(book, input, EDIT);
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
        List<T> finishedBooks = JSONHandler.IO.readJSONFile(input.getPath(), FINISHED, language);

        if (finishedBooks.size() != 0) {
            setIdsAndDates(finishedBooks);

            service.clearAll();
            // both additional dates table and books table are updated
            service.addAll(finishedBooks);
        }
    }

    protected void save(PathInput input, Language language) {
        List<T> bookToReadList = service.findAll();
        JSONHandler.IO.saveTableToJSON(bookToReadList, input.getPath(), language, FINISHED);
    }

    @Override
    protected String showTable(Input input, Model model, Language language) {
        List<T> bookList = filterAndSort();

        model.addAttribute("books", bookList);
        model.addAttribute("additionalDates", additionalDatesService.findAll());
        model.addAttribute("finished" + language.firstLetterToUpperCase() + "Input", input);

        return "finishedBooks" + language.firstLetterToUpperCase();
    }
}
