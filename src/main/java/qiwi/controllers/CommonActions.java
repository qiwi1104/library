package qiwi.controllers;

import qiwi.model.common.Input;
import qiwi.model.common.book.Book;
import qiwi.model.common.book.FinishedBook;

import java.util.List;

public abstract class CommonActions {
    public static <T extends Book> void setBookAttributesFromInput(T book, Input input, String context) {
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

                    if (input.getStartDescription().length() != 0) {
                        fb.setStartDescription(input.getStartDescription());
                    }

                    if (input.getEndDescription().length() != 0) {
                        fb.setEndDescription(input.getEndDescription());
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

                if (input.getFoundDescription().length() != 0) {
                    book.setFoundDescription(input.getFoundDescription());
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
                book.setFoundDescription(input.getFoundDescription());

                if (book instanceof FinishedBook) {
                    FinishedBook fb = (FinishedBook) book;

                    fb.setStartDescription(input.getStartDescription());
                    fb.setEndDescription(input.getEndDescription());
                }
                break;
        }

    }

    public static <T extends FinishedBook> boolean isInTable(T book, List<T> collection) {
        for (FinishedBook finishedBook : collection) {
            if (finishedBook.equals(book)) {
                book.setId(finishedBook.getId());
                book.setFound(finishedBook.getFound());
                return true;
            }
        }

        return false;
    }
}
