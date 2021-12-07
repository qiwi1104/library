package qiwi.controllers.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import qiwi.Application;
import qiwi.model.book.Book;
import qiwi.model.book.BookToRead;
import qiwi.model.book.FinishedBook;
import qiwi.model.input.Input;
import qiwi.util.enums.BookType;
import qiwi.util.enums.Context;
import qiwi.util.enums.Language;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
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
                        try {
                            books = (List<T>) Arrays.asList(mapper.readValue(Paths.get(path).toFile(), FinishedBook[].class));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case TO_READ:
                        try {
                            books = (List<T>) Arrays.asList(mapper.readValue(Paths.get(path).toFile(), BookToRead[].class));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                return books;
            }
        }

        private static String getPathToBackupDirectory(BookType bookType) {
            Properties property = new Properties();

            try (Reader inputStream = new InputStreamReader(Application.class.getResourceAsStream("/config.properties"), StandardCharsets.UTF_8)) {
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

                String languageStr = language.toLowerCase();
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
                    if (file.lastModified() > lastModifiedTime && file.getName().toLowerCase().contains(language.toLowerCase())) {
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

    protected <T extends Book, S extends Input> void setBookAttributesFromInput(T book, S input, Context context, Language language) {
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
                if (book instanceof FinishedBook) {
                    ((FinishedBook) book).setStart(input.getStart());
                    ((FinishedBook) book).setEnd(input.getEnd());
                }
                book.setAuthor(input.getAuthor());
                book.setName(input.getName());
                book.setFound(input.getFound());
                book.setDescription(input.getDescription());
                book.setLanguage(language.firstLetterToUpperCase());
                break;
        }
    }

    protected abstract String showTable(Model model, Language language);

    protected abstract boolean edit(Input input, Model model, Language language);

    protected String getRedirectionAddress(Input input, BindingResult result, Model model, Language language, String bookType) {
        if (result.hasErrors()) {
            if (input.getId() == null) {
                return showTable(model, language);
            } else {
                if (edit(input, model, language))
                    return "redirect:/" + bookType + "/" + language.toLowerCase() + "/";
                else return showTable(model, language);
            }
        }

        if (edit(input, model, language))
            return "redirect:/" + bookType + "/" + language.toLowerCase() + "/";
        else return showTable(model, language);
    }
}
