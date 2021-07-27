package qiwi.controllers.common;

import qiwi.model.common.Input;
import qiwi.model.common.book.Book;
import qiwi.model.common.book.FinishedBook;

public class BookController {
    public <T extends Book> void setBookAttributesFromInput(T book, Input input, String context) {
        switch (context) {
            case "edit":
                if (book instanceof FinishedBook) {
                    FinishedBook fb = (FinishedBook) book;

                    if (input.getStart().toString().length() != 0) {
                        fb.setStart(input.getStart());
                    }

                    if (input.getEnd().toString().length() != 0) {
                        fb.setEnd(input.getEnd());
                    }
                }

                if (input.getAuthor().length() != 0) {
                    book.setAuthor(input.getAuthor());
                }

                if (input.getName().length() != 0) {
                    book.setName(input.getName());
                }

                if (input.getFound().toString().length() != 0) {
                    book.setFound(input.getFound());
                }

                if (input.getDescription().length() != 0) {
                    book.setDescription(input.getDescription());
                }
                break;
            case "addFirst": // first потому что используется в первый раз (до isInTable)
                book.setAuthor(input.getAuthor());
                book.setName(input.getName());

                if (book instanceof FinishedBook) {
                    FinishedBook fb = (FinishedBook) book;

                    fb.setStart(input.getStart());
                    fb.setEnd(input.getEnd());
                }
                break;
            case "addSecond":
                book.setFound(input.getFound());
                book.setDescription(input.getDescription());
                break;
        }
    }
}
