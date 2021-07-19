package qiwi.controllers.spanish;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.IO;
import qiwi.TimeFormat;
import qiwi.controllers.CommonActions;
import qiwi.model.common.Input;
import qiwi.model.spanish.BookToReadSpanish;
import qiwi.model.spanish.FinishedBookSpanish;
import qiwi.service.spanish.BookToReadSpanishServiceImpl;
import qiwi.service.spanish.FinishedBookSpanishServiceImpl;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/bookstoread/spanish")
public class BookToReadSpanishController {
    @Autowired
    private BookToReadSpanishServiceImpl service;
    @Autowired
    private FinishedBookSpanishServiceImpl finishedBookService;

    private String sortDateMethod = "ASC";
    private String sortProperty = "found";

    private List<BookToReadSpanish> filterAndSort() {
        List<BookToReadSpanish> books = null;

        switch (sortDateMethod) {
            case "ASC":
                switch (sortProperty) {
                    case "id":
                        books = service.findAllByOrderByIdAsc();
                        break;
                    case "found":
                        books = service.findAllByOrderByFoundAsc();
                        break;
                }
                break;
            case "DESC":
                switch (sortProperty) {
                    case "id":
                        books = service.findAllByOrderByIdDesc();
                        break;
                    case "found":
                        books = service.findAllByOrderByFoundDesc();
                        break;
                }
                break;
        }
        return books;
    }

    private List<BookToReadSpanish> fillList(JSONArray source) {
        List<BookToReadSpanish> destination = new ArrayList<>();

        for (int i = 0; i < source.length(); i++) {
            BookToReadSpanish bookToAdd = new BookToReadSpanish();

            bookToAdd.setId(i + 1);
            bookToAdd.setAuthor(source.getJSONObject(i).get("author").toString());
            bookToAdd.setName(source.getJSONObject(i).get("name").toString());

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d", source.getJSONObject(i).get("found").toString());

                bookToAdd.setFound(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setDescription(source.getJSONObject(i).get("found").toString()
                        + "\n" + source.getJSONObject(i).get("description").toString());

                bookToAdd.setFound(Date.valueOf("1970-1-1"));
            }

            bookToAdd.setDescription(source.getJSONObject(i).get("description").toString());

            destination.add(bookToAdd);
        }

        return destination;
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadSpanishInput") Input input) {
        BookToReadSpanish book = new BookToReadSpanish();

        book.setId(service.findAll().size() + 1);
        CommonActions.setBookAttributesFromInput(book, input, "addFirst");
        CommonActions.setBookAttributesFromInput(book, input, "addSecond");

        service.addBook(book);

        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadSpanishInput") Input input) {
        BookToReadSpanish book = service.getBookById(input.getId());

        CommonActions.setBookAttributesFromInput(book, input, "edit");

        service.addBook(book);

        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);

        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@ModelAttribute("booksToReadSpanishInput") Input input) {
        BookToReadSpanish bookToRead = service.getBookById(input.getId());

        FinishedBookSpanish book = new FinishedBookSpanish(bookToRead.getAuthor(), bookToRead.getName(), input.getStart(), input.getEnd(),
                bookToRead.getFound(), bookToRead.getDescription());
        book.setId(finishedBookService.findAll().size() + 1);

        finishedBookService.addBook(book);
        service.deleteBook(bookToRead.getId());

        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        sortDateMethod = sortDateMethod.equals("ASC") ? "DESC" : "ASC";
        sortProperty = property;

        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("booksToReadSpanishInput") Input input) throws IOException {
        service.clearAll();
        List<BookToReadSpanish> bookToReadList;

        JSONArray jsonArray = IO.readJSONFile(input.getName());
        bookToReadList = fillList(jsonArray);

        service.addAll(bookToReadList);

        return "redirect:/bookstoread/spanish/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("booksToReadSpanishInput") Input input) {
        List<BookToReadSpanish> bookToReadList = service.findAll();

        CommonActions.saveTableToJSON(bookToReadList, input.getName(), "Spanish");

        return "redirect:/bookstoread/spanish/";
    }

    @GetMapping("/")
    public String list(Model model) {
        List<BookToReadSpanish> bookList = filterAndSort();

        model.addAttribute("booksToRead", bookList);

        return "booksToReadSpanish";
    }
}
