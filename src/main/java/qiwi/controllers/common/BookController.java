package qiwi.controllers.common;

import qiwi.model.book.Book;
import qiwi.model.book.FinishedBook;
import qiwi.model.input.Input;
import qiwi.util.enums.Action;
import qiwi.util.enums.Language;

public abstract class BookController {
    protected <T extends Book, S extends Input> void setBookAttributesFromInput(
            T book, S input, Action action, Language language) {
        switch (action) {
            case EDIT:
                if (book instanceof FinishedBook) {
                    if (!input.getStart().equals(java.sql.Date.valueOf("1970-1-1"))) {
                        ((FinishedBook) book).setStart(input.getStart());
                    }

                    if (!input.getEnd().equals(java.sql.Date.valueOf("1970-1-1"))) {
                        ((FinishedBook) book).setEnd(input.getEnd());
                    }
                }

                if (input.getAuthor().length() != 0) {
                    book.setAuthor(input.getAuthor());
                }

                if (input.getName().length() != 0) {
                    book.setName(input.getName());
                }

                if (!input.getFound().equals(java.sql.Date.valueOf("1970-1-1"))) {
                    book.setFound(input.getFound());
                }

                if (input.getDescription().length() != 0) {
                    book.setDescription(input.getDescription());
                }
                break;
            case ADD:
                if (book instanceof FinishedBook) {
                    ((FinishedBook) book).setStart(input.getStart());
                    ((FinishedBook) book).setEnd(input.getEnd());
                }
                book.setAuthor(input.getAuthor());
                book.setName(input.getName());
                book.setFound(input.getFound());
                book.setDescription(input.getDescription());
                book.setLanguage(language.firstLetterToUpperCase());
                break;
        }
    }
}
