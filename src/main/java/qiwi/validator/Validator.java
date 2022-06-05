package qiwi.validator;

import org.springframework.validation.Errors;
import qiwi.model.book.Book;

public class Validator implements org.springframework.validation.Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;

        if (book.getName() == null || book.getName().isBlank()) {
            errors.reject("name", "Name is missing.");
        }

        if (book.getFound() == null) {
            errors.reject("date", "Date is missing.");
        }
    }
}
