package qiwi.controllers.common;

import org.json.JSONArray;
import org.json.JSONObject;
import qiwi.Application;
import qiwi.TimeFormat;
import qiwi.controllers.enums.BookType;
import qiwi.controllers.enums.Context;
import qiwi.controllers.enums.Language;
import qiwi.model.common.AdditionalDates;
import qiwi.model.common.Input;
import qiwi.model.common.book.Book;
import qiwi.model.common.book.BookToRead;
import qiwi.model.common.book.FinishedBook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import static qiwi.controllers.enums.BookType.*;

public abstract class BookController {
    protected static class JSONHandler {
        /*
         * Converts Entity books to JSON books and vice versa
         */
        protected static class Conversion {
            private static <T extends Book> void setCommonAttributes(JSONObject jsonBook, T book) {
                jsonBook.put("author", book.getAuthor());
                jsonBook.put("name", book.getName());

                String found = TimeFormat.formatTime("yyyy-M-d", "M/d/yy", book.getFound().toString());
                jsonBook.put("found", found);
                jsonBook.put("description", book.getDescription());
            }

            private static <T extends Book> void fillJSONArray(JSONArray jsonArray, List<T> booksList) {
                if (booksList.get(0) instanceof BookToRead) {
                    for (T book : booksList) {
                        JSONObject jsonBook = new JSONObject();
                        setCommonAttributes(jsonBook, book);
                        jsonArray.put(jsonBook);
                    }
                } else {
                    System.out.println("Something went wrong :(");
                }
            }

            private static <T extends Book, S extends AdditionalDates> void fillJSONArray(
                    JSONArray jsonArray, List<T> booksList, List<S> additionalDates) {
                if (booksList.get(0) instanceof FinishedBook) {
                    for (T book : booksList) {
                        FinishedBook finishedBook = (FinishedBook) book;
                        JSONObject jsonBook = new JSONObject();
                        setCommonAttributes(jsonBook, finishedBook);

                        JSONArray dates = new JSONArray();
                        String start = TimeFormat.formatTime("yyyy-M-d", "M/d/yy", finishedBook.getStart().toString());
                        String end = TimeFormat.formatTime("yyyy-M-d", "M/d/yy", finishedBook.getEnd().toString());

                        dates.put(start);
                        dates.put(end);
                        jsonBook.put("dates", dates);

                        JSONObject additionalDatesJSON = new JSONObject();
                        JSONArray additionalDatesStartJSON = new JSONArray();
                        JSONArray additionalDatesEndJSON = new JSONArray();

                        for (AdditionalDates additionalDate : additionalDates) {
                            if (additionalDate.getFinishedBookId().equals(finishedBook.getId())) {
                                additionalDatesStartJSON.put(TimeFormat.formatTime("yyyy-M-d", "M/d/yy", additionalDate.getStart().toString()));
                                additionalDatesEndJSON.put(TimeFormat.formatTime("yyyy-M-d", "M/d/yy", additionalDate.getEnd().toString()));
                            }
                        }

                        additionalDatesJSON.put("start", additionalDatesStartJSON);
                        additionalDatesJSON.put("end", additionalDatesEndJSON);
                        jsonBook.put("additional_dates", additionalDatesJSON);

                        jsonArray.put(jsonBook);
                    }
                } else {
                    System.out.println("Something went wrong :(");
                }
            }
        }
        /*
        * Handles writing to/reading from file
        * */
        protected static class IO {
            private static void writeJSONArrayToFile(JSONArray array, String path) {
                String res = array.toString();

                try {
                    FileWriter writer = new FileWriter(new File(path), StandardCharsets.UTF_8);
                    writer.write(res);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            protected static <T extends Book> void saveTableToJSON(List<T> booksList, String filePath, Language language) {
                JSONArray jsonArray = new JSONArray();
                Conversion.fillJSONArray(jsonArray, booksList);
                writeJSONArrayToFile(jsonArray, fixPathToBackupFile(filePath, language, TO_READ));
            }

            protected static <T extends Book, S extends AdditionalDates> void saveTableToJSON(
                    List<T> booksList, List<S> additionalDates, String filePath, Language language) {
                JSONArray jsonArray = new JSONArray();
                Conversion.fillJSONArray(jsonArray, booksList, additionalDates);
                writeJSONArrayToFile(jsonArray, fixPathToBackupFile(filePath, language, FINISHED));
            }

            protected static JSONArray readJSONFile(String path) {
                Scanner scan;
                StringBuilder str = new StringBuilder();

                try {
                    scan = new Scanner(new File(path), StandardCharsets.UTF_8);

                    while (scan.hasNext()) {
                        str.append(scan.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return new JSONArray();
                }

                scan.close();
                return new JSONArray(str.toString());
            }
        }

        private static String getPathToBackupDirectory(BookType bookType) {
            Properties property = new Properties();

            try {
                FileInputStream inputStream = new FileInputStream(
                        String.valueOf((Application.class
                                .getClassLoader()
                                .getResource("config.properties")
                                .toString()
                                .split("file:/")[1])));

                property.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch (bookType) {
                case TO_READ:
                    return property.getProperty("booksToRead.path");
                case FINISHED:
                    return property.getProperty("finishedBooks.path");
                default:
                    return "";
            }
        }

        private static String fixPathToBackupFile(String filePath, Language language, BookType bookType) {
            if (filePath.isEmpty()) {
                filePath = getPathToBackupDirectory(bookType);
            } else {
                if (!filePath.endsWith("\\")) {
                    filePath += "\\";
                }
            }

            String languageStr = language.toString().toLowerCase();
            languageStr = languageStr.substring(0, 1).toUpperCase() + languageStr.substring(1);

            filePath += languageStr + " " + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + ".json";
            return filePath;
        }

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
