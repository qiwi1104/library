package qiwi.controllers.common;

import qiwi.controllers.enums.Sort;
import qiwi.controllers.enums.SortBy;
import qiwi.model.common.book.FinishedBook;
import qiwi.service.common.FinishedBookServiceImpl;

import java.util.List;

import static qiwi.controllers.enums.Sort.ASC;
import static qiwi.controllers.enums.SortBy.START;

public class FinishedBookController<T extends FinishedBook, S extends FinishedBookServiceImpl<T, ?>> extends BookController {
    protected Sort sortDateMethod = ASC;
    protected SortBy sortProperty = START;

    protected List<T> filterAndSort(S service) {
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
                }
                break;
        }
        return books;
    }
}
