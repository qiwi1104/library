package qiwi.controllers.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import qiwi.Application;
import qiwi.model.common.book.Book;
import qiwi.model.common.book.FinishedBook;
import qiwi.model.common.input.FinishedBookInput;
import qiwi.model.common.input.Input;
import qiwi.model.english.BookToReadEnglish;
import qiwi.model.english.FinishedBookEnglish;
import qiwi.model.russian.BookToReadRussian;
import qiwi.model.russian.FinishedBookRussian;
import qiwi.model.spanish.BookToReadSpanish;
import qiwi.model.spanish.FinishedBookSpanish;
import qiwi.util.enums.BookType;
import qiwi.util.enums.Context;
import qiwi.util.enums.Language;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class BookController {
    protected static class JSONHandler {
        /*
         * Handles writing to/reading from file
         * */
        protected static class IO {
            protected static <T extends Book> void saveTableToJSON(List<T> booksList, String path, Language language, BookType type) {
                try {
                    ObjectWriter writer = new ObjectMapper().setTimeZone(TimeZone.getDefault()).writer().withDefaultPrettyPrinter();
                    writer.writeValue(Paths.get(fixPathToBackupFile(path, language, type)).toFile(), booksList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public static <T extends Book> List<T> readJSONFile(String path, BookType bookType, Language language) {
                // path to a folder/file has not been set, meaning the one from config.properties will be used
                if (path.equals("")) {
                    path = getPathToBackupDirectory(bookType);

                    if (!path.equals("")) {
                        path += getLatestFileName(path, language);
                    } else {
                        System.out.println("Paths in config.properties might not have been defined :(");
                        return new ArrayList<>();
                    }
                } else {
                    if (!path.endsWith(".json")) { // path to a certain file is not defined
                        if (!path.endsWith("\\")) { // path to a folder containing the file to create table from
                            path += "\\";
                        }
                    }
                }

                ObjectMapper mapper = new ObjectMapper();
                List<T> books = new ArrayList<>();

                switch (bookType) {
                    case FINISHED:
                        switch (language) {
                            case ENGLISH:
                                try {
                                    books = (List<T>) Arrays.asList(mapper.readValue(Paths.get(path).toFile(), FinishedBookEnglish[].class));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case RUSSIAN:
                                try {
                                    books = (List<T>) Arrays.asList(mapper.readValue(Paths.get(path).toFile(), FinishedBookRussian[].class));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case SPANISH:
                                try {
                                    books = (List<T>) Arrays.asList(mapper.readValue(Paths.get(path).toFile(), FinishedBookSpanish[].class));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                    case TO_READ:
                        switch (language) {
                            case ENGLISH:
                                try {
                                    books = (List<T>) Arrays.asList(mapper.readValue(Paths.get(path).toFile(), BookToReadEnglish[].class));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case RUSSIAN:
                                try {
                                    books = (List<T>) Arrays.asList(mapper.readValue(Paths.get(path).toFile(), BookToReadRussian[].class));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case SPANISH:
                                try {
                                    books = (List<T>) Arrays.asList(mapper.readValue(Paths.get(path).toFile(), BookToReadSpanish[].class));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                }

                return books;
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
                    return property.getOrDefault("booksToRead.path", "").toString();
                case FINISHED:
                    return property.getOrDefault("finishedBooks.path", "").toString();
                default:
                    return "";
            }
        }

        private static String fixPathToBackupFile(String path, Language language, BookType bookType) {
            if (!path.endsWith(".json")) {
                if (path.equals("")) {
                    path = getPathToBackupDirectory(bookType);
                    if (path.equals("")) {
                        System.out.println("Paths in config.properties might not have been defined :(");
                        return "";
                    }
                } else {
                    if (!path.endsWith("\\")) {
                        path += "\\";
                    }
                }

                String languageStr = language.toString().toLowerCase();
                languageStr = languageStr.substring(0, 1).toUpperCase() + languageStr.substring(1);

                path += languageStr + " " + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + ".json";
            }

            return path;
        }

        private static String getLatestFileName(String path, Language language) {
            File directory = new File(path);
            File[] files = directory.listFiles(File::isFile);
            long lastModifiedTime = Long.MIN_VALUE;
            File chosenFile = null;
            if (files != null) {
                for (File file : files) {
                    if (file.lastModified() > lastModifiedTime && file.getName().toLowerCase().contains(language.toString().toLowerCase())) {
                        chosenFile = file;
                        lastModifiedTime = file.lastModified();
                    }
                }
            }

            String fileName = "";
            if (chosenFile != null) {
                fileName = chosenFile.getName();
            }

            return fileName;
        }
    }

    public static class TimeFormat {
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

    protected <T extends Book, S extends Input> void setBookAttributesFromInput(T book, S input, Context context) {
        switch (context) {
            case EDIT:
                if (book instanceof FinishedBook) {
                    FinishedBook fb = (FinishedBook) book;
                    FinishedBookInput finishedBookInput = (FinishedBookInput) input;

                    if (!finishedBookInput.getStart().equals(java.sql.Date.valueOf("1970-1-1"))) { // дата введена, т.е. ее надо менять
                        fb.setStart(finishedBookInput.getStart());
                    }

                    if (!finishedBookInput.getEnd().equals(java.sql.Date.valueOf("1970-1-1"))) { // дата введена, т.е. ее надо менять
                        fb.setEnd(finishedBookInput.getEnd());
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
                    FinishedBookInput finishedBookInput = (FinishedBookInput) input;

                    fb.setStart(finishedBookInput.getStart());
                    fb.setEnd(finishedBookInput.getEnd());
                }

                book.setFound(input.getFound());
                book.setDescription(input.getDescription());
                break;
        }
    }

    protected abstract <T extends Book> List<T> filterAndSort();
}
