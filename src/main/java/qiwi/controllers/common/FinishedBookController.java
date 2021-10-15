package qiwi.controllers.common;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import qiwi.util.enums.*;
import qiwi.util.Factory;
import qiwi.model.common.AdditionalDates;
import qiwi.util.enums.TypeAndLanguage;
import qiwi.model.common.book.FinishedBook;
import qiwi.model.common.input.FinishedBookInput;
import qiwi.model.common.input.PathInput;
import qiwi.repository.common.AdditionalDatesRepository;
import qiwi.repository.common.FinishedBookRepository;
import qiwi.service.common.AdditionalDatesServiceImpl;
import qiwi.service.common.FinishedBookServiceImpl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static qiwi.controllers.common.BookController.JSONHandler.Conversion.setAttributes;
import static qiwi.util.enums.BookType.FINISHED;
import static qiwi.util.enums.Context.*;
import static qiwi.util.enums.SortBy.*;
import static qiwi.util.enums.SortType.*;

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

    private void fillAdditionalDatesTable(JSONArray source, TypeAndLanguage type) {
        for (int i = 0; i < source.length(); i++) {
            if (source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("start").length() != 0) {
                for (T finishedBook : service.findAll()) {
                    if (finishedBook.getName().equals(source.getJSONObject(i).get("name"))) {
                        U additionalDates = Factory.createDates(type);

                        additionalDates.setId(additionalDatesService.findAll().size() + 1);
                        additionalDates.setFinishedBookId(finishedBook.getId());
                        for (int j = 0; j < source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("start").length(); j++) {
                            String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                                    source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("start").get(j).toString());

                            String date2 = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                                    source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("end").get(j).toString());

                            additionalDates.setStart(Date.valueOf(date));
                            additionalDates.setEnd(Date.valueOf(date2));
                            additionalDatesService.addDates(additionalDates);
                        }
                    }
                }
            }
        }
    }

    private List<T> fillList(JSONArray source, TypeAndLanguage type) {
        List<T> destination = new ArrayList<>();

        for (int i = 0; i < source.length(); i++) {
            T bookToAdd = Factory.createBook(type);
            setAttributes(bookToAdd, source.getJSONObject(i), i + 1);

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
        JSONArray jsonBooks = JSONHandler.IO.readJSONFile(input.getPath(), FINISHED, language);
        if (jsonBooks.length() != 0) {
            service.clearAll();

            List<T> finishedBooks = fillList(jsonBooks, TypeAndLanguage.valueOf(FINISHED + "_" + language));

            service.addAll(finishedBooks);
            fillAdditionalDatesTable(jsonBooks, TypeAndLanguage.valueOf("DATES_" + language)); // добавляет доп. даты в свою таблицу
        } else {
            System.out.println("The list is empty :(");
        }
    }

    protected void save(PathInput input, Language language) {
        List<T> bookToReadList = service.findAll();
        List<U> additionalDatesList = additionalDatesService.findAll();

        JSONHandler.IO.saveTableToJSON(bookToReadList, additionalDatesList, input.getPath(), language);
    }

    protected void list(Model model, List<T> bookList) {
        bookList = filterAndSort();

        model.addAttribute("books", bookList);
        model.addAttribute("additionalDates", additionalDatesService.findAll());
    }
}
