package qiwi.controllers.english;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.IO;
import qiwi.TimeFormat;
import qiwi.controllers.CommonActions;
import qiwi.model.common.Input;
import qiwi.model.english.BookToRead;
import qiwi.model.english.FinishedBook;
import qiwi.service.english.BookToReadServiceImpl;
import qiwi.service.english.FinishedBookServiceImpl;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/bookstoread/english")
public class BookToReadController {
    @Autowired
    private BookToReadServiceImpl service;
    @Autowired
    private FinishedBookServiceImpl finishedBookService;

    private String sortDateMethod = "ASC";
    private String sortProperty = "found";

    private List<BookToRead> filterAndSort() {
        List<BookToRead> books = null;

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

    private List<BookToRead> fillList(JSONArray source) {
        List<BookToRead> destination = new ArrayList<>();

        for (int i = 0; i < source.length(); i++) {
            BookToRead bookToAdd = new BookToRead();

            bookToAdd.setId(i + 1);
            bookToAdd.setAuthor(source.getJSONObject(i).get("author").toString());
            bookToAdd.setName(source.getJSONObject(i).get("name").toString());

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d", source.getJSONObject(i).get("found").toString());

                bookToAdd.setFound(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setFoundDescription(source.getJSONObject(i).get("found").toString()
                        + "\n" + source.getJSONObject(i).get("found_description").toString());

                bookToAdd.setFound(Date.valueOf("1970-1-1"));
            }

            bookToAdd.setFoundDescription(source.getJSONObject(i).get("found_description").toString());

            destination.add(bookToAdd);
        }

        return destination;
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadEnglishInput") Input input) {
        BookToRead book = new BookToRead();

        book.setId(service.findAll().size() + 1);
        CommonActions.setBookAttributesFromInput(book, input, "addFirst");
        CommonActions.setBookAttributesFromInput(book, input, "addSecond");

        service.addBook(book);

        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadEnglishInput") Input input) {
        BookToRead book = service.getBookById(input.getId());

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
        BookToRead bookToRead = service.getBookById(input.getId());

        FinishedBook book = new FinishedBook(bookToRead.getAuthor(), bookToRead.getName(), input.getStart(), input.getEnd(),
                "", "", bookToRead.getFound(), bookToRead.getFoundDescription());
        book.setId(finishedBookService.findAll().size() + 1);

        finishedBookService.addBook(book);
        service.deleteBook(bookToRead.getId());

        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        sortDateMethod = sortDateMethod.equals("ASC") ? "DESC" : "ASC";
        sortProperty = property;

        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("booksToReadEnglishInput") Input input) throws IOException {
        service.clearAll();
        List<BookToRead> bookToReadList;

        JSONArray jsonArray = IO.readJSONFile(input.getName());
        bookToReadList = fillList(jsonArray);

        service.addAll(bookToReadList);

        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("booksToReadEnglishInput") Input input) {
        List<BookToRead> bookToReadList = service.findAll();

        CommonActions.saveTableToJSON(bookToReadList, input.getName());

        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/")
    public String list(Model model) {
        List<BookToRead> bookList = filterAndSort();

        model.addAttribute("booksToRead", bookList);

        return "booksToReadEnglish";
    }
}
