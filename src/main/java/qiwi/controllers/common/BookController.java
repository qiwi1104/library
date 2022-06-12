package qiwi.controllers.common;

import qiwi.validator.BookValidator;

public abstract class BookController {
    protected final BookValidator validator = new BookValidator();
}
