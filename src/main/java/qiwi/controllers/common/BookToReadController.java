package qiwi.controllers.common;

import qiwi.controllers.enums.Sort;
import qiwi.controllers.enums.SortBy;
import qiwi.model.common.book.BookToRead;
import qiwi.service.common.BookToReadServiceImpl;

import java.util.List;

import static qiwi.controllers.enums.Sort.ASC;
import static qiwi.controllers.enums.SortBy.FOUND;

public class BookToReadController<T extends BookToRead, S extends BookToReadServiceImpl<T, ?>> extends BookController {
    protected Sort sortDateMethod = ASC;
    protected SortBy sortProperty = FOUND;

    protected List<T> filterAndSort(S service) {
        List<T> books = null;

        switch (sortDateMethod) {
            case ASC:
                switch (sortProperty) {
                    case ID:
                        books = service.findAllByOrderByIdAsc();
                        break;
                    case FOUND:
                        books = service.findAllByOrderByFoundAsc();
                        break;
                }
                break;
            case DESC:
                switch (sortProperty) {
                    case ID:
                        books = service.findAllByOrderByIdDesc();
                        break;
                    case FOUND:
                        books = service.findAllByOrderByFoundDesc();
                        break;
                }
                break;
        }
        return books;
    }
}
