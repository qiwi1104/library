package qiwi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import qiwi.Application;
import qiwi.model.book.Book;
import qiwi.model.book.BookToRead;
import qiwi.model.book.FinishedBook;
import qiwi.model.enums.BookType;
import qiwi.model.enums.Language;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class JSONHandler {

    /*
     * Handles writing to/reading from file
     * */
    public static class IO {
        public static <T extends Book> void saveTableToJSON(
                List<T> booksList, String path, Language language, BookType type) {
            try {
                ObjectWriter writer = new ObjectMapper().setTimeZone(TimeZone.getDefault()).writer().withDefaultPrettyPrinter();
                writer.writeValue(Paths.get(fixPathToBackupFile(path, language, type)).toFile(), booksList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static <T extends Book> List<T> readJSONFile(
                String path, BookType bookType, Language language) {
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

                    path+= getLatestFileName(path, language);
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            List<T> books = new ArrayList<>();

            switch (bookType) {
                case FINISHED:
                    try {
                        books = (List<T>) Arrays.asList(mapper.readValue(Paths.get(path).toFile(), FinishedBook[].class));

                        for (T book : books) {
                            FinishedBook finishedBook = (FinishedBook) book;

                            if (finishedBook.getAdditionalDates().size() != 0) {
                                finishedBook.getAdditionalDates()
                                        .forEach(dates -> dates.setFinishedBook(finishedBook));
                            }
                        }
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

        try (Reader inputStream = new InputStreamReader(Application.class.getResourceAsStream("/config.properties"),
                StandardCharsets.UTF_8)) {
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
