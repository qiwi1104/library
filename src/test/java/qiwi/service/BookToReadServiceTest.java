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
import qiwi.model.book.BookToRead;
import qiwi.model.book.FinishedBook;
import qiwi.model.enums.Language;
import qiwi.service.dao.BookToReadDAO;
import qiwi.service.dao.FinishedBookDAO;
import qiwi.service.service.BookToReadService;
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
import static qiwi.model.enums.BookType.TO_READ;

@SpringBootTest
class BookToReadServiceTest {
    private final BookToReadService bookToReadService = new BookToReadService();
    private final FinishedBookService finishedBookService = new FinishedBookService();

    @Autowired
    private BookToReadDAO bookToReadDAO;
    @Autowired
    private FinishedBookDAO finishedBookDAO;

    private List<BookToRead> expected;
    private BookToRead book;

    private BindingResult bindingResult;
    private Model model;
    private final Language LANGUAGE = Language.ENGLISH;
    private static final String BACKUP_FOLDER = "src/test/resources/backups/books-to-read/";
    private static final String LAST_BACKUP = "src/test/resources/backups/books-to-read/English 2023-01-23 22-58-00.json";

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        Field decField = BookToReadService.class.getDeclaredField("bookToReadDAO");
        decField.setAccessible(true);
        decField.set(bookToReadService, bookToReadDAO);

        bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        model = mock(Model.class);

        expected = new ArrayList<>();

        book = new BookToRead();
        book.setAuthor("a");
        book.setName("b");
        book.setFound(Date.valueOf("2023-01-21"));
        book.setDescription("");
        book.setLanguage(LANGUAGE.firstLetterToUpperCase());
    }

    @AfterEach
    void clearList() {
        bookToReadService.findAllByOrderByIdAsc(LANGUAGE).forEach(b -> bookToReadService.deleteBookById(b.getId()));
    }

    @Test
    void testAddBook() {
        bookToReadService.addBook(book, bindingResult, model, LANGUAGE);

        expected.add(book);

        assertTrue(expected.containsAll(bookToReadService.findAllByOrderByIdAsc(LANGUAGE)));
    }

    @Test
    void testAddExistingBook() {
        bookToReadService.addBook(book, bindingResult, model, LANGUAGE);
        bookToReadService.addBook(book, bindingResult, model, LANGUAGE);

        assertEquals(1, bookToReadService.findAllByOrderByIdAsc(LANGUAGE).size());
    }

    @Test
    void testEditBook() {
        bookToReadService.addBook(book, bindingResult, model, LANGUAGE);

        BookToRead book2 = new BookToRead();
        book2.setId(book.getId());
        book2.setAuthor(book.getAuthor());
        book2.setName(book.getName());
        book2.setFound(Date.valueOf("2023-01-21"));
        book2.setDescription("");
        book2.setLanguage(LANGUAGE.firstLetterToUpperCase());

        expected.add(book2);

        bookToReadService.editBook(book2, bindingResult, model, LANGUAGE);

        List<BookToRead> actual = bookToReadService.findAllByOrderByIdAsc(LANGUAGE);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void testEditBookChangingToExistingBook() {
        bookToReadService.addBook(book, bindingResult, model, LANGUAGE);

        BookToRead book2 = new BookToRead();
        book2.setAuthor("a");
        book2.setName("c");
        book2.setFound(Date.valueOf("2023-01-21"));
        book2.setDescription("");
        book2.setLanguage(LANGUAGE.firstLetterToUpperCase());

        bookToReadService.addBook(book2, bindingResult, model, LANGUAGE);

        expected.add(book);
        expected.add(book2);

        BookToRead book3 = new BookToRead();
        book3.setId(book.getId());
        book3.setAuthor("a");
        book3.setName("c");
        book3.setFound(Date.valueOf("2023-01-21"));
        book3.setDescription("");
        book3.setLanguage(LANGUAGE.firstLetterToUpperCase());

        bookToReadService.editBook(book3, bindingResult, model, LANGUAGE);

        List<BookToRead> actual = bookToReadService.findAllByOrderByIdAsc(LANGUAGE);

        assertTrue(expected.containsAll(actual));
        assertEquals(2, actual.size());
    }

    @Test
    void testFinishBook() throws IllegalAccessException, NoSuchFieldException {
        Field decField = BookToReadService.class.getDeclaredField("finishedBookDAO");
        decField.setAccessible(true);
        decField.set(bookToReadService, finishedBookDAO);

        decField = FinishedBookService.class.getDeclaredField("finishedBookDAO");
        decField.setAccessible(true);
        decField.set(finishedBookService, finishedBookDAO);

        bookToReadService.addBook(book, bindingResult, model, LANGUAGE);

        FinishedBook finishedBook = new FinishedBook();
        finishedBook.setAuthor(book.getAuthor());
        finishedBook.setName(book.getName());
        finishedBook.setFound(book.getFound());
        finishedBook.setDescription(book.getDescription());
        finishedBook.setLanguage(book.getLanguage());
        finishedBook.setStart(Date.valueOf("2023-01-23"));
        finishedBook.setEnd(Date.valueOf("2023-01-23"));

        bookToReadService.finishBook(finishedBook, book.getId());

        assertTrue(bookToReadService.findAllByOrderByIdAsc(LANGUAGE).isEmpty());
        assertTrue(finishedBookService.findAllByOrderByIdAsc(LANGUAGE).contains(finishedBook));
    }

    @Test
    void testGetBookById() {
        bookToReadService.addBook(book, bindingResult, model, LANGUAGE);

        assertEquals(book, bookToReadService.getBookById(book.getId()));
    }

    @Test
    void testDeleteBookById() {
        bookToReadService.addBook(book, bindingResult, model, LANGUAGE);

        bookToReadService.deleteBookById(book.getId());

        assertTrue(bookToReadService.findAllByOrderByIdAsc(LANGUAGE).isEmpty());
    }

    private void performLoadTest(PathDTO input) {
        bookToReadService.load(input, LANGUAGE);

        expected = JSONHandler.IO.readJSONFile(LAST_BACKUP, TO_READ, LANGUAGE);
        List<BookToRead> actual = bookToReadService.findAllByOrderByIdAsc(LANGUAGE);

        assertTrue(actual.containsAll(expected));
        assertTrue(expected.containsAll(actual));
        assertEquals(75, actual.size());
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

    @Test
    void testLoadBatch() {
        PathDTO input = new PathDTO();
        input.setPath("");

        bookToReadService.load(input, LANGUAGE);

        List<BookToRead> beforeLoadBatch = bookToReadService.findAllByOrderByIdAsc(LANGUAGE);

        input.setPath(BACKUP_FOLDER + "loadBatch.json");

        List<BookToRead> loadedBooks = JSONHandler.IO.readJSONFile(input.getPath(), TO_READ, LANGUAGE);

        bookToReadService.loadBatch(input, LANGUAGE);

        List<BookToRead> afterLoadBatch = bookToReadService.findAllByOrderByIdAsc(LANGUAGE);

        assertTrue(afterLoadBatch.containsAll(beforeLoadBatch));
        assertTrue(afterLoadBatch.containsAll(loadedBooks));
    }

    private void performSaveTest(PathDTO input) {
        PathDTO loadInput = new PathDTO();
        loadInput.setPath(LAST_BACKUP);

        bookToReadService.load(loadInput, LANGUAGE);
        bookToReadService.save(input, LANGUAGE);

        expected = JSONHandler.IO.readJSONFile(LAST_BACKUP, TO_READ, LANGUAGE);

        bookToReadService.load(input, LANGUAGE);

        List<BookToRead> actual = bookToReadService.findAllByOrderByIdAsc(LANGUAGE);

        assertTrue(actual.containsAll(expected));
        assertTrue(expected.containsAll(actual));
        assertEquals(75, actual.size());
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
        File directory = new File(BookToReadServiceTest.BACKUP_FOLDER);
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