package qiwi.util;

import qiwi.model.common.AdditionalDates;
import qiwi.model.common.book.Book;
import qiwi.model.english.*;
import qiwi.util.enums.TypeAndLanguage;
import qiwi.model.russian.*;
import qiwi.model.spanish.*;

public class Factory<T extends Book, S extends AdditionalDates> {
    public static <T> T createBook(TypeAndLanguage type) {
        T book;

        switch (type) {
            case TO_READ_ENGLISH:
                book = (T) new BookToReadEnglish();
                break;
            case TO_READ_RUSSIAN:
                book = (T) new BookToReadRussian();
                break;
            case TO_READ_SPANISH:
                book = (T) new BookToReadSpanish();
                break;
            case FINISHED_ENGLISH:
                book = (T) new FinishedBookEnglish();
                break;
            case FINISHED_RUSSIAN:
                book = (T) new FinishedBookRussian();
                break;
            case FINISHED_SPANISH:
                book = (T) new FinishedBookSpanish();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        return book;
    }

    public static <S> S createDates(TypeAndLanguage type) {
        S dates;

        switch (type) {
            case DATES_ENGLISH:
                dates = (S) new AdditionalDatesEnglish();
                break;
            case DATES_RUSSIAN:
                dates = (S) new AdditionalDatesRussian();
                break;
            case DATES_SPANISH:
                dates = (S) new AdditionalDatesSpanish();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        return dates;
    }
}
