package qiwi.validator;

import org.springframework.validation.Errors;
import qiwi.model.book.FinishedBook;

public class FinishedBookValidator extends BookValidator {
    @Override
    public void validate(Object o, Errors errors) {
        FinishedBook book = (FinishedBook) o;

        super.validate(book, errors);

        if (book.getStart() == null || book.getEnd() == null) {
            errors.reject("date", "Date is missing");
        }
    }
}
