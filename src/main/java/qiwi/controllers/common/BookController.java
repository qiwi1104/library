package qiwi.controllers.common;

import org.json.JSONArray;
import org.json.JSONObject;
import qiwi.Application;
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
import java.text.ParseException;
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
         * */
        protected static class Conversion {
            /*
            * Sets properties to a Finished Book/Book To Read
            * */
            protected static <T extends Book> void setAttributes(T bookToAdd, JSONObject jsonBook, int id) {
                bookToAdd.setId(id);
                bookToAdd.setAuthor(jsonBook.get("author").toString());
                bookToAdd.setName(jsonBook.get("name").toString());

                if (bookToAdd instanceof FinishedBook) {
                    FinishedBook fb = (FinishedBook) bookToAdd;

                    try {
                        String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                                jsonBook.getJSONArray("dates").get(0).toString());

                        fb.setStart(java.sql.Date.valueOf(date));
                    } catch (Exception e) {
                        fb.setStart(java.sql.Date.valueOf("1970-1-1"));
                    }

                    try {
                        String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                                jsonBook.getJSONArray("dates").get(1).toString());

                        fb.setEnd(java.sql.Date.valueOf(date));
                    } catch (Exception e) {
                        fb.setEnd(java.sql.Date.valueOf("1970-1-1"));
                    }
                }

                try {
                    String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d", jsonBook.get("found").toString());
                    bookToAdd.setFound(java.sql.Date.valueOf(date));
                } catch (Exception e) {
                    bookToAdd.setFound(java.sql.Date.valueOf("1970-1-1"));
                }

                bookToAdd.setDescription(jsonBook.get("description").toString());
            }

            /*
             * Sets properties shared by both Books To Read and Finished Books to a JSON book
             * */
            private static <T extends Book> void setCommonAttributes(JSONObject jsonBook, T book) {
                jsonBook.put("author", book.getAuthor());
                jsonBook.put("name", book.getName());

                String found = TimeFormat.formatTime("yyyy-M-d", "M/d/yy", book.getFound().toString());
                jsonBook.put("found", found);
                jsonBook.put("description", book.getDescription());
            }

            /*
             * Fills JSON array with Books To Read
             * */
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

            /*
             * Fills JSON array with Finished Books
             * */
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

            protected static JSONArray readJSONFile(String path, BookType bookType) {
                if (path.equals("")) {
                    path = getPathToBackupDirectory(bookType);
                    path += getLatestFileName(path);
                }

                return readJSONFile(path);
            }
        }

        private static String getPathToBackupDirectory(BookType bookType) {
            Properties property = new Properties();

            try {
                Reader inputStream = new InputStreamReader(Application.class.getResourceAsStream("/config.properties"), StandardCharsets.UTF_8);
                property.load(inputStream);
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

        private static String getLatestFileName(String path) {
            File directory = new File(path);
            File[] files = directory.listFiles(File::isFile);
            long lastModifiedTime = Long.MIN_VALUE;
            File chosenFile = null;
            if (files != null) {
                for (File file : files) {
                    if (file.lastModified() > lastModifiedTime) {
                        chosenFile = file;
                        lastModifiedTime = file.lastModified();
                    }
                }
            }

            return chosenFile.getName();
        }
    }

    protected static class TimeFormat {
        public static String formatTime(String oldFormat, String newFormat, String stringToParse) {
            SimpleDateFormat oldDateFormat = new SimpleDateFormat(oldFormat);
            SimpleDateFormat newDateFormat = new SimpleDateFormat(newFormat);

            Date date;

            try {
                date = oldDateFormat.parse(stringToParse);
            } catch (ParseException e) {
                date = java.sql.Date.valueOf("1970-1-1");
                e.printStackTrace();
            }

            return newDateFormat.format(date);
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
            case ADD:
                book.setAuthor(input.getAuthor());
                book.setName(input.getName());

                if (book instanceof FinishedBook) {
                    FinishedBook fb = (FinishedBook) book;

                    fb.setStart(input.getStart());
                    fb.setEnd(input.getEnd());
                }

                book.setFound(input.getFound());
                book.setDescription(input.getDescription());
                break;
        }
    }

    protected abstract <T extends Book> List<T> filterAndSort();
}
