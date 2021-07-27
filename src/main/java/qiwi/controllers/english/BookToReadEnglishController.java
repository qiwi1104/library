package qiwi.controllers.english;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.IO;
import qiwi.TimeFormat;
import qiwi.controllers.CommonActions;
import qiwi.controllers.enums.Sort;
import qiwi.controllers.enums.SortBy;
import qiwi.model.common.Input;
import qiwi.model.english.BookToReadEnglish;
import qiwi.model.english.FinishedBookEnglish;
import qiwi.service.english.BookToReadEnglishServiceImpl;
import qiwi.service.english.FinishedBookEnglishServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static qiwi.controllers.enums.Sort.ASC;
import static qiwi.controllers.enums.Sort.DESC;
import static qiwi.controllers.enums.SortBy.FOUND;

@Controller
@RequestMapping("/bookstoread/english")
public class BookToReadEnglishController {
    @Autowired
    private BookToReadEnglishServiceImpl service;
    @Autowired
    private FinishedBookEnglishServiceImpl finishedBookService;

    private Sort sortDateMethod = ASC;
    private SortBy sortProperty = FOUND;

    private List<BookToReadEnglish> filterAndSort() {
        List<BookToReadEnglish> books = null;

        switch (sortDateMethod) {
            case ASC:
                switch (sortProperty) {
                    case ID:
                        books = service.findAllByOrderByIdAsc();
                        break;
                    case FOUND:
                        books = service.findAllByOrderByFoundAsc();
                        break;
                }
                break;
            case DESC:
                switch (sortProperty) {
                    case ID:
                        books = service.findAllByOrderByIdDesc();
                        break;
                    case FOUND:
                        books = service.findAllByOrderByFoundDesc();
                        break;
                }
                break;
        }
        return books;
    }

    private List<BookToReadEnglish> fillList(JSONArray source) {
        List<BookToReadEnglish> destination = new ArrayList<>();

        for (int i = 0; i < source.length(); i++) {
            BookToReadEnglish bookToAdd = new BookToReadEnglish();

            bookToAdd.setId(i + 1);
            bookToAdd.setAuthor(source.getJSONObject(i).get("author").toString());
            bookToAdd.setName(source.getJSONObject(i).get("name").toString());

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d", source.getJSONObject(i).get("found").toString());

                bookToAdd.setFound(java.sql.Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setFound(java.sql.Date.valueOf("1970-1-1"));
            }

            bookToAdd.setDescription(source.getJSONObject(i).get("description").toString());

            destination.add(bookToAdd);
        }

        return destination;
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadEnglishInput") Input input) {
        BookToReadEnglish book = new BookToReadEnglish();

        book.setId(service.findAll().size() + 1);
        CommonActions.setBookAttributesFromInput(book, input, "addFirst");
        CommonActions.setBookAttributesFromInput(book, input, "addSecond");

        service.addBook(book);

        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadEnglishInput") Input input) {
        BookToReadEnglish book = service.getBookById(input.getId());

        CommonActions.setBookAttributesFromInput(book, input, "edit");

        service.addBook(book);

        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);

        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@ModelAttribute("booksToReadEnglishInput") Input input) {
        BookToReadEnglish bookToRead = service.getBookById(input.getId());

        FinishedBookEnglish book = new FinishedBookEnglish(bookToRead.getAuthor(), bookToRead.getName(), input.getStart(), input.getEnd(),
                bookToRead.getFound(), bookToRead.getDescription());
        book.setId(finishedBookService.findAll().size() + 1);

        finishedBookService.addBook(book);
        service.deleteBook(bookToRead.getId());

        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        sortDateMethod = sortDateMethod.equals(ASC) ? DESC : ASC;
        sortProperty = SortBy.valueOf(property.toUpperCase());

        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("booksToReadEnglishInput") Input input) throws IOException {
        service.clearAll();
        List<BookToReadEnglish> bookToReadList;

        JSONArray jsonArray = IO.readJSONFile(input.getName());
        bookToReadList = fillList(jsonArray);

        service.addAll(bookToReadList);

        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("booksToReadEnglishInput") Input input) {
        List<BookToReadEnglish> bookToReadList = service.findAll();

        CommonActions.saveTableToJSON(bookToReadList, input.getName(), "English");

        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/")
    public String list(Model model) {
        List<BookToReadEnglish> bookList = filterAndSort();

        model.addAttribute("booksToRead", bookList);

        return "booksToReadEnglish";
    }
}
