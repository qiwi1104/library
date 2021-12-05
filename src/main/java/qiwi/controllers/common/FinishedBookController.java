package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import qiwi.model.AdditionalDates;
import qiwi.model.book.FinishedBook;
import qiwi.model.input.Input;
import qiwi.model.input.PathInput;
import qiwi.service.impl.AdditionalDatesServiceImpl;
import qiwi.service.impl.FinishedBookServiceImpl;
import qiwi.util.enums.Language;
import qiwi.util.enums.SortBy;
import qiwi.util.enums.SortType;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static qiwi.util.enums.BookType.FINISHED;
import static qiwi.util.enums.Context.ADD;
import static qiwi.util.enums.Context.EDIT;
import static qiwi.util.enums.SortBy.START;
import static qiwi.util.enums.SortType.ASC;
import static qiwi.util.enums.SortType.DESC;

public abstract class FinishedBookController extends BookController {
    @Autowired
    protected FinishedBookServiceImpl service;
    @Autowired
    protected AdditionalDatesServiceImpl additionalDatesService;

    protected SortType sortDateMethod = ASC;
    protected SortBy sortProperty = START;

    /*
     * Sets ids to Additional Dates of a book
     * */
    private void setIdsAndDates(List<FinishedBook> books) {
        int booksCounter = service.findAll().size() + 1;
        int datesCounter = additionalDatesService.findAll().size() + 1;

        for (FinishedBook book : books) {
            book.setId(booksCounter);

            if (book.getAdditionalDates().size() != 0) {
                List<AdditionalDates> dates = book.getAdditionalDates();

                for (AdditionalDates date : dates) {
                    date.setId(datesCounter);
                    date.setFinishedBookId(book.getId());

                    datesCounter++;
                }
            }

            booksCounter++;
        }
    }

    /*
     * Returns either a redirection link to the respective page (if there are no errors)
     * Or a view name (if there are errors)
     * */
    protected String getRedirectionAddress(Input input, BindingResult result, Model model, Language language, FinishedBook book, AdditionalDates additionalDates) {
        if (result.hasErrors())
            return showTable(model, language, service.findAll());

        if (add(input, model, book, additionalDates))
            return "redirect:/finishedbooks/" + language.toLowerCase() + "/";
        else return showTable(model, language, service.findAll());
    }

    protected List<FinishedBook> sortList(List<FinishedBook> list) {
        switch (sortDateMethod) {
            case ASC:
                switch (sortProperty) {
                    case ID:
                        list.sort(Comparator.comparing(FinishedBook::getId).thenComparing(FinishedBook::getId));
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
                        list.sort(Comparator.comparing(FinishedBook::getId).thenComparing(FinishedBook::getId));
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

    protected boolean add(Input input, Model model, FinishedBook book, AdditionalDates additionalDates) {
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
        FinishedBook book = service.getBookById(input.getId());

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

    @Transactional
    protected void load(PathInput input, Language language) {
        List<FinishedBook> finishedBooks = JSONHandler.IO.readJSONFile(input.getPath(), FINISHED, language);

        if (finishedBooks.size() != 0) {
            service.clearLanguage(language);
            additionalDatesService.computeIds();

            setIdsAndDates(finishedBooks);

//            List<AdditionalDates> dates = finishedBooks
//                    .stream()
//                    .filter(b -> b.getAdditionalDates().size() != 0)
//                    .map(FinishedBook::getAdditionalDates)
//                    .flatMap(Collection::stream)
//                    .collect(Collectors.toList());

            // both additional dates table and books table are updated
            service.addAll(finishedBooks);
//            dates.forEach(d -> additionalDatesService.addDates(d));
        }
    }

    protected void save(PathInput input, Language language) {
        List<FinishedBook> bookToReadList = service.findAll();
        JSONHandler.IO.saveTableToJSON(bookToReadList, input.getPath(), language, FINISHED);
    }

    @Override
    protected String showTable(Model model, Language language, List<FinishedBook> books) {
        List<FinishedBook> bookList = service.findAllByOrderByIdAsc(language);
        books.clear();

        int i = 1;
        for (FinishedBook book : bookList) {
            if (book.getLanguage().equals(language.firstLetterToUpperCase())) {
                FinishedBook newBook = book.clone();
                newBook.setId(i);

                for (AdditionalDates date : newBook.getAdditionalDates())
                    date.setFinishedBookId(newBook.getId());

                books.add(newBook);
                i++;
            }
        }

        books = sortList(books);

        model.addAttribute("books", books);
        model.addAttribute("additionalDates", additionalDatesService.findAll());
        model.addAttribute("finished" + language.firstLetterToUpperCase() + "Input", new Input());

        return "finishedBooks" + language.firstLetterToUpperCase();
    }
}
