package qiwi.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import qiwi.dto.PathDTO;
import qiwi.model.AdditionalDate;
import qiwi.model.book.FinishedBook;
import qiwi.model.enums.Language;
import qiwi.service.dao.FinishedBookDAO;
import qiwi.service.service.FinishedBookService;
import qiwi.util.JSONHandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static qiwi.model.enums.BookType.FINISHED;

@SpringBootTest
public class FinishedBookServiceTest {
    private final FinishedBookService finishedBookService = new FinishedBookService();
    @Autowired
    private FinishedBookDAO finishedBookDAO;

    private List<FinishedBook> expected;
    private FinishedBook book;

    private BindingResult bindingResult;
    private Model model;
    private final Language LANGUAGE = Language.ENGLISH;
    private static final String BACKUP_FOLDER = "src/test/resources/backups/finished-books/";
    private static final String LAST_BACKUP = "src/test/resources/backups/finished-books/English 2023-01-23 22-58-00.json";

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        Field decField = FinishedBookService.class.getDeclaredField("finishedBookDAO");
        decField.setAccessible(true);
        decField.set(finishedBookService, finishedBookDAO);

        bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        model = mock(Model.class);

        expected = new ArrayList<>();

        book = new FinishedBook();
        book.setAuthor("a");
        book.setName("b");
        book.setFound(Date.valueOf("2023-01-21"));
        book.setStart(Date.valueOf("2023-01-21"));
        book.setEnd(Date.valueOf("2023-01-21"));
        book.setDescription("");
        book.setAdditionalDates(new ArrayList<>());
        book.setLanguage(LANGUAGE.firstLetterToUpperCase());
    }

    @AfterEach
    void clearList() {
        finishedBookService.findAllByOrderByIdAsc(LANGUAGE).forEach(b -> finishedBookService.deleteBookById(b.getId()));
    }

    @Test
    void testAddBook() {
        finishedBookService.addBook(book, bindingResult, model, LANGUAGE);

        expected.add(book);

        assertEquals(1, finishedBookService.findAllByOrderByIdAsc(LANGUAGE).size());
    }

    @Test
    void testAddExistingBook() {
        finishedBookService.addBook(book, bindingResult, model, LANGUAGE);
        book = finishedBookService.getBookById(book.getId());
        finishedBookService.addBook(book, bindingResult, model, LANGUAGE);

        assertEquals(1, finishedBookService.findAllByOrderByIdAsc(LANGUAGE).size());
    }

    @Test
    void testAddAdditionalDates() {
        finishedBookService.addBook(book, bindingResult, model, LANGUAGE);

        FinishedBook bookToBeAdded = new FinishedBook();
        bookToBeAdded.setAuthor("a");
        bookToBeAdded.setName("b");
        bookToBeAdded.setFound(Date.valueOf("2023-01-21"));
        bookToBeAdded.setStart(Date.valueOf("2023-01-23"));
        bookToBeAdded.setEnd(Date.valueOf("2023-01-23"));
        bookToBeAdded.setDescription("");
        bookToBeAdded.setAdditionalDates(new ArrayList<>());
        bookToBeAdded.setLanguage(LANGUAGE.firstLetterToUpperCase());

        AdditionalDate additionalDate = new AdditionalDate();

        additionalDate.setStart(Date.valueOf("2023-01-23"));
        additionalDate.setEnd(Date.valueOf("2023-01-23"));

        bookToBeAdded.addDate(additionalDate);

        finishedBookService.addBook(bookToBeAdded, bindingResult, model, LANGUAGE);

        FinishedBook updatedBook = finishedBookService.getBookById(book.getId());

        assertTrue(updatedBook.hasDate(additionalDate));

        assertEquals(book.getStart(), updatedBook.getStart());
        assertEquals(book.getEnd(), updatedBook.getEnd());
        assertEquals(book.getFound(), updatedBook.getFound());
    }

    @Test
    void testEditBook() {
        finishedBookService.addBook(book, bindingResult, model, LANGUAGE);

        FinishedBook book2 = new FinishedBook();
        book2.setId(book.getId());
        book2.setAuthor("a");
        book2.setName("c");
        book2.setFound(Date.valueOf("2023-01-21"));
        book2.setStart(Date.valueOf("2023-01-21"));
        book2.setEnd(Date.valueOf("2023-01-21"));
        book2.setDescription("");
        book2.setAdditionalDates(new ArrayList<>());
        book2.setLanguage(LANGUAGE.firstLetterToUpperCase());

        expected.add(book2);

        finishedBookService.editBook(book2, bindingResult, model, LANGUAGE);

        List<FinishedBook> actual = finishedBookService.findAllByOrderByIdAsc(LANGUAGE);

        assertTrue(actual.containsAll(expected));
        assertTrue(expected.containsAll(actual));
        assertEquals(1, actual.size());
    }

    @Test
    void testEditBookChangingToExistingBook() {
        finishedBookService.addBook(book, bindingResult, model, LANGUAGE);

        FinishedBook book2 = new FinishedBook();
        book2.setAuthor("a");
        book2.setName("c");
        book2.setFound(Date.valueOf("2023-01-21"));
        book2.setStart(Date.valueOf("2023-01-21"));
        book2.setEnd(Date.valueOf("2023-01-21"));
        book2.setDescription("");
        book2.setAdditionalDates(new ArrayList<>());
        book2.setLanguage(LANGUAGE.firstLetterToUpperCase());

        finishedBookService.addBook(book2, bindingResult, model, LANGUAGE);

        expected.add(book);
        expected.add(book2);

        FinishedBook book3 = new FinishedBook();
        book3.setId(book.getId());
        book3.setAuthor("a");
        book3.setName("c");
        book3.setFound(Date.valueOf("2023-01-21"));
        book3.setStart(Date.valueOf("2023-01-21"));
        book3.setEnd(Date.valueOf("2023-01-21"));
        book3.setDescription("");
        book3.setAdditionalDates(new ArrayList<>());
        book3.setLanguage(LANGUAGE.firstLetterToUpperCase());

        finishedBookService.editBook(book3, bindingResult, model, LANGUAGE);

        List<FinishedBook> actual = finishedBookService.findAllByOrderByIdAsc(LANGUAGE);

        assertTrue(actual.containsAll(expected));
        assertTrue(expected.containsAll(actual));
        assertEquals(2, actual.size());
    }

    @Test
    void testGetBookById() {
        finishedBookService.addBook(book, bindingResult, model, LANGUAGE);

        assertEquals(book, finishedBookService.getBookById(book.getId()));
    }

    @Test
    void testDeleteBookById() {
        finishedBookService.addBook(book, bindingResult, model, LANGUAGE);

        finishedBookService.deleteBookById(book.getId());

        assertTrue(finishedBookService.findAllByOrderByIdAsc(LANGUAGE).isEmpty());
    }

    private void performLoadTest(PathDTO input) {
        finishedBookService.load(input, LANGUAGE);

        expected = JSONHandler.IO.readJSONFile(LAST_BACKUP, FINISHED, LANGUAGE);
        List<FinishedBook> actual = finishedBookService.findAllByOrderByIdAsc(LANGUAGE);

        assertTrue(actual.containsAll(expected));
        assertTrue(expected.containsAll(actual));
        assertEquals(2, actual.size());
    }

    @Test
    void testPredefinedPathLoad() {
        PathDTO input = new PathDTO();
        input.setPath("");

        performLoadTest(input);
    }

    @Test
    void testInputPathToFolderLoad() {
        PathDTO input = new PathDTO();
        input.setPath(BACKUP_FOLDER);

        performLoadTest(input);
    }

    @Test
    void testInputPathToFileLoad() {
        PathDTO input = new PathDTO();
        input.setPath(LAST_BACKUP);

        performLoadTest(input);
    }

    private void performSaveTest(PathDTO input) {
        PathDTO loadInput = new PathDTO();
        loadInput.setPath(LAST_BACKUP);

        finishedBookService.load(loadInput, LANGUAGE);
        finishedBookService.save(input, LANGUAGE);

        expected = JSONHandler.IO.readJSONFile(LAST_BACKUP, FINISHED, LANGUAGE);

        finishedBookService.load(input, LANGUAGE);

        List<FinishedBook> actual = finishedBookService.findAllByOrderByIdAsc(LANGUAGE);

        assertTrue(actual.containsAll(expected));
        assertTrue(expected.containsAll(actual));
        assertEquals(2, actual.size());
    }

    @Test
    void testInputPathSave() {
        PathDTO input = new PathDTO();
        input.setPath(BACKUP_FOLDER + "saveTest.json");

        performSaveTest(input);
    }

    @Test
    void testInputPathToFolderSave() {
        PathDTO input = new PathDTO();
        input.setPath(BACKUP_FOLDER);

        performSaveTest(input);
    }

    @AfterAll
    static void deleteCreatedFile() {
        try {
            Path path = Path.of(BACKUP_FOLDER + getLatestFileName());
            if (!LAST_BACKUP.contains(getLatestFileName())) {
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getLatestFileName() {
        File directory = new File(BACKUP_FOLDER);
        File[] files = directory.listFiles(File::isFile);
        long lastModifiedTime = Long.MIN_VALUE;
        File chosenFile = null;
        if (files != null) {
            for (File file : files) {
                if (file.lastModified() > lastModifiedTime && file.getName().toLowerCase().contains(Language.ENGLISH.toLowerCase())) {
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
