package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import qiwi.model.common.AdditionalDates;
import qiwi.model.common.book.FinishedBook;
import qiwi.model.common.input.FinishedBookInput;
import qiwi.model.common.input.PathInput;
import qiwi.repository.common.AdditionalDatesRepository;
import qiwi.repository.common.FinishedBookRepository;
import qiwi.service.common.AdditionalDatesServiceImpl;
import qiwi.service.common.FinishedBookServiceImpl;
import qiwi.util.Factory;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;
import qiwi.util.enums.SortType;
import qiwi.util.enums.TypeAndLanguage;

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

    private void fillAdditionalDatesTableNew(TypeAndLanguage type) {
        for (T bookFromJSON : service.findAll()) {
            if (bookFromJSON.getAdditionalDates().size() != 0) {
                U additionalDates = Factory.createDates(type);

                additionalDates.setId(additionalDatesService.findAll().size() + 1);
                additionalDates.setFinishedBookId(bookFromJSON.getId());

                List<U> additionalDatesList = (List<U>) bookFromJSON.getAdditionalDates();
                for (int i = 0; i < additionalDatesList.size(); i++) {
                    additionalDates.setStart(additionalDatesList.get(i).getStart());
                    additionalDates.setEnd(additionalDatesList.get(i).getEnd());
                }

                additionalDatesService.addDates(additionalDates);
            }
        }
    }

    private void setAttributesNew(List<T> books) {
        int i = 1;
        for (T book : books) {
            book.setId(i);

            if (book.getAdditionalDates().size() != 0) {
                List<U> additionalDatesList = (List<U>) book.getAdditionalDates();
                for (int j = 0; j < additionalDatesList.size(); j++) {
                    U additionalDates = additionalDatesList.get(j);

                    additionalDates.setFinishedBookId(i);
                    additionalDates.setId(j + 1);
                    additionalDates.setStart(additionalDatesList.get(j).getStart());
                    additionalDates.setEnd(additionalDatesList.get(j).getEnd());
                }
            }

            i++;
        }
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

    protected void add(FinishedBookInput input, T book, U additionalDates) {
        book.setId(service.findAll().size() + 1);
        setBookAttributesFromInput(book, input, ADD);

        if (service.exists(book)) {
            book.setId(service.get(book).getId());
            book.setFound(service.get(book).getFound());

            additionalDates.setId(additionalDatesService.findAll().size() + 1);
            additionalDates.setFinishedBookId(book.getId());
            additionalDates.setStart(book.getStart());
            additionalDates.setEnd(book.getEnd());

            additionalDatesService.addDates(additionalDates);
        } else {
            service.addBook(book);
        }
    }

    protected void edit(FinishedBookInput input) {
        T book = service.getBookById(input.getId());
        setBookAttributesFromInput(book, input, EDIT);
        service.addBook(book);
    }

    protected void sort(SortBy sortProperty) {
        sortDateMethod = sortDateMethod.equals(ASC) ? DESC : ASC;
        this.sortProperty = sortProperty;
    }

    protected void load(PathInput input, Language language) {
        List<T> finishedBooks = JSONHandler.IO.newReadJSONFile(input.getPath(), FINISHED, language);

        if (finishedBooks.size() != 0) {
            setAttributesNew(finishedBooks);

            service.clearAll();
            service.addAll(finishedBooks);

            fillAdditionalDatesTableNew(TypeAndLanguage.valueOf("DATES_" + language));
        }
    }

    protected void save(PathInput input, Language language) {
        List<T> bookToReadList = service.findAll();
        JSONHandler.IO.finishedBookTableSaveToJSON(bookToReadList, input.getPath(), language);
    }

    protected void list(Model model, List<T> bookList) {
        bookList = filterAndSort();

        model.addAttribute("books", bookList);
        model.addAttribute("additionalDates", additionalDatesService.findAll());
    }
}
