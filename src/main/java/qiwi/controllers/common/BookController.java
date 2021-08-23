package qiwi.controllers.common;

import org.json.JSONArray;
import org.json.JSONObject;
import qiwi.IO;
import qiwi.TimeFormat;
import qiwi.controllers.enums.Context;
import qiwi.controllers.enums.Language;
import qiwi.model.common.AdditionalDates;
import qiwi.model.common.Input;
import qiwi.model.common.book.Book;
import qiwi.model.common.book.BookToRead;
import qiwi.model.common.book.FinishedBook;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class BookController {
    private <T extends Book> void fillJSONArray(JSONArray jsonArray, List<T> booksList) {
        if (booksList.get(0) instanceof BookToRead) {
            for (T book : booksList) {
                JSONObject jsonBook = new JSONObject();

                jsonBook.put("author", book.getAuthor());
                jsonBook.put("name", book.getName());
                String found = "";

                try {
                    found = TimeFormat.formatTime("yyyy-M-d", "M/d/yy", book.getFound().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                jsonBook.put("found", found);
                jsonBook.put("description", book.getDescription());

                jsonArray.put(jsonBook);
            }
        } else {
            System.out.println("Something went wrong :(");
        }
    }

    private <T extends Book, S extends AdditionalDates> void fillJSONArray(JSONArray jsonArray, List<T> booksList, List<S> additionalDates) {
        if (booksList.get(0) instanceof FinishedBook) {
            for (T book : booksList) {
                FinishedBook finishedBook = (FinishedBook) book;
                JSONObject jsonBook = new JSONObject();

                jsonBook.put("author", finishedBook.getAuthor());
                jsonBook.put("name", finishedBook.getName());

                JSONArray dates = new JSONArray();
                String start = "";
                String end = "";

                try {
                    start = TimeFormat.formatTime("yyyy-M-d", "M/d/yy", finishedBook.getStart().toString());
                    end = TimeFormat.formatTime("yyyy-M-d", "M/d/yy", finishedBook.getEnd().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                dates.put(start);
                dates.put(end);
                jsonBook.put("dates", dates);

                JSONObject additionalDatesJSON = new JSONObject();
                JSONArray additionalDatesStartJSON = new JSONArray();
                JSONArray additionalDatesEndJSON = new JSONArray();

                for (AdditionalDates additionalDate : additionalDates) {
                    if (additionalDate.getFinishedBookId().equals(finishedBook.getId())) {
                        try {
                            additionalDatesStartJSON.put(TimeFormat.formatTime("yyyy-M-d", "M/d/yy", additionalDate.getStart().toString()));
                            additionalDatesEndJSON.put(TimeFormat.formatTime("yyyy-M-d", "M/d/yy", additionalDate.getEnd().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                additionalDatesJSON.put("start", additionalDatesStartJSON);
                additionalDatesJSON.put("end", additionalDatesEndJSON);
                jsonBook.put("additional_dates", additionalDatesJSON);

                String found = "";

                try {
                    found = TimeFormat.formatTime("yyyy-M-d", "M/d/yy", finishedBook.getFound().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                jsonBook.put("found", found);
                jsonBook.put("description", finishedBook.getDescription());

                jsonArray.put(jsonBook);
            }
        } else {
            System.out.println("Something went wrong :(");
        }
    }

    private void writeToFile(String filePath, Language language, JSONArray jsonArray) {
        if (!filePath.endsWith("\\")) {
            filePath += "\\";
        }

        String languageStr = language.toString().toLowerCase();
        languageStr = languageStr.substring(0, 1).toUpperCase() + languageStr.substring(1);

        filePath += languageStr + " " + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + ".json";
        try {
            IO.writeJSONToFile(jsonArray, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected <T extends Book> void saveTableToJSON(List<T> booksList, String filePath, Language language) {
        JSONArray jsonArray = new JSONArray();
        fillJSONArray(jsonArray, booksList);
        writeToFile(filePath, language, jsonArray);
    }

    protected <T extends Book, S extends AdditionalDates> void saveTableToJSON(List<T> booksList, List<S> additionalDates, String filePath, Language language) {
        JSONArray jsonArray = new JSONArray();
        fillJSONArray(jsonArray, booksList, additionalDates);
        writeToFile(filePath, language, jsonArray);
    }

    protected <T extends Book> void setBookAttributesFromInput(T book, Input input, Context context) {
        switch (context) {
            case EDIT:
                if (book instanceof FinishedBook) {
                    FinishedBook fb = (FinishedBook) book;

                    if (!input.getStart().equals(java.sql.Date.valueOf("1970-1-1"))) { // дата введена, т.е. ее надо менять
                        fb.setStart(input.getStart());
                    }

                    if (!input.getEnd().equals(java.sql.Date.valueOf("1970-1-1"))) { // дата введена, т.е. ее надо менять
                        fb.setEnd(input.getEnd());
                    }
                }

                if (input.getAuthor().length() != 0) {
                    book.setAuthor(input.getAuthor());
                }

                if (input.getName().length() != 0) {
                    book.setName(input.getName());
                }

                if (!input.getFound().equals(java.sql.Date.valueOf("1970-1-1"))) { // дата введена, т.е. ее надо менять
                    book.setFound(input.getFound());
                }

                if (input.getDescription().length() != 0) {
                    book.setDescription(input.getDescription());
                }
                break;
            case ADD_FIRST: // first потому что используется в первый раз (до isInTable)
                book.setAuthor(input.getAuthor());
                book.setName(input.getName());

                if (book instanceof FinishedBook) {
                    FinishedBook fb = (FinishedBook) book;

                    fb.setStart(input.getStart());
                    fb.setEnd(input.getEnd());
                }
                break;
            case ADD_SECOND:
                book.setFound(input.getFound());
                book.setDescription(input.getDescription());
                break;
        }
    }

    protected abstract <T extends Book> List<T> filterAndSort();
}
