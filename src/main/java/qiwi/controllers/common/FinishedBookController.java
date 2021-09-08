package qiwi.controllers.common;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import qiwi.TimeFormat;
import qiwi.controllers.enums.Language;
import qiwi.controllers.enums.SortBy;
import qiwi.controllers.enums.SortType;
import qiwi.model.common.AdditionalDates;
import qiwi.model.common.Input;
import qiwi.model.common.book.FinishedBook;
import qiwi.model.english.AdditionalDatesEnglish;
import qiwi.model.english.FinishedBookEnglish;
import qiwi.model.russian.AdditionalDatesRussian;
import qiwi.model.russian.FinishedBookRussian;
import qiwi.model.spanish.AdditionalDatesSpanish;
import qiwi.model.spanish.FinishedBookSpanish;
import qiwi.repository.common.AdditionalDatesRepository;
import qiwi.service.common.AdditionalDatesServiceImpl;
import qiwi.service.common.FinishedBookServiceImpl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static qiwi.controllers.enums.Context.*;
import static qiwi.controllers.enums.SortBy.*;
import static qiwi.controllers.enums.SortType.*;

public abstract class FinishedBookController<
        T extends FinishedBook,
        S extends FinishedBookServiceImpl<T, ?>,
        U extends AdditionalDates,
        V extends AdditionalDatesServiceImpl<U, ? extends AdditionalDatesRepository<U>>> extends BookController {
    @Autowired
    protected S service;
    @Autowired
    protected V additionalDatesService;

    protected SortType sortDateMethod = ASC;
    protected SortBy sortProperty = START;

    private void fillAdditionalDatesTable(JSONArray source, Language language) {
        for (int i = 0; i < source.length(); i++) {
            if (source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("start").length() != 0) {
                for (T finishedBook : service.findAll()) {
                    if (finishedBook.getName().equals(source.getJSONObject(i).get("name"))) {
                        U additionalDates;

                        switch (language) {
                            case ENGLISH:
                                additionalDates = (U) new AdditionalDatesEnglish();
                                break;
                            case RUSSIAN:
                                additionalDates = (U) new AdditionalDatesRussian();
                                break;
                            case SPANISH:
                                additionalDates = (U) new AdditionalDatesSpanish();
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + language);
                        }

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

    private List<T> fillList(JSONArray source, Language language) {
        List<T> destination = new ArrayList<>();

        for (int i = 0; i < source.length(); i++) {
            T bookToAdd;

            switch (language) {
                case ENGLISH:
                    bookToAdd = (T) new FinishedBookEnglish();
                    break;
                case RUSSIAN:
                    bookToAdd = (T) new FinishedBookRussian();
                    break;
                case SPANISH:
                    bookToAdd = (T) new FinishedBookSpanish();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + language);
            }

            bookToAdd.setId(i + 1);
            bookToAdd.setAuthor(source.getJSONObject(i).get("author").toString());
            bookToAdd.setName(source.getJSONObject(i).get("name").toString());

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                        source.getJSONObject(i).getJSONArray("dates").get(0).toString());

                bookToAdd.setStart(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setStart(Date.valueOf("1970-1-1"));
            }

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                        source.getJSONObject(i).getJSONArray("dates").get(1).toString());

                bookToAdd.setEnd(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setEnd(Date.valueOf("1970-1-1"));
            }

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d", source.getJSONObject(i).get("found").toString());
                bookToAdd.setFound(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setFound(Date.valueOf("1970-1-1"));
            }

            bookToAdd.setDescription(source.getJSONObject(i).get("description").toString());

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

    protected void add(Input inputFinished, T book, U additionalDates) {
        book.setId(service.findAll().size() + 1);
        setBookAttributesFromInput(book, inputFinished, ADD);

        if (service.isInTable(book)) {
            additionalDates.setId(additionalDatesService.findAll().size() + 1);
            additionalDates.setFinishedBookId(book.getId());
            additionalDates.setStart(book.getStart());
            additionalDates.setEnd(book.getEnd());

            additionalDatesService.addDates(additionalDates);
        } else {
            service.addBook(book);
        }
    }

    protected void edit(Input inputFinished, T book) {
        setBookAttributesFromInput(book, inputFinished, EDIT);
        service.addBook(book);
    }

    protected void sort(SortBy sortProperty) {
        sortDateMethod = sortDateMethod.equals(ASC) ? DESC : ASC;
        this.sortProperty = sortProperty;
    }

    protected void load(Input input, Language language) {
        JSONArray jsonBooks = JSONHandler.IO.readJSONFile(input.getName());
        if (jsonBooks.length() != 0) {
            service.clearAll();

            List<T> finishedBooks = fillList(jsonBooks, language);

            service.addAll(finishedBooks);
            fillAdditionalDatesTable(jsonBooks, language); // добавляет доп. даты в свою таблицу
        } else {
            System.out.println("The list is empty :(");
        }
    }

    protected void save(Input input, Language language) {
        List<T> bookToReadList = service.findAll();
        List<U> additionalDatesList = additionalDatesService.findAll();

        JSONHandler.IO.saveTableToJSON(bookToReadList, additionalDatesList, input.getName(), language);
    }

    protected void list(Model model, List<T> bookList) {
        bookList = filterAndSort();

        model.addAttribute("books", bookList);
        model.addAttribute("additionalDates", additionalDatesService.findAll());
    }
}
