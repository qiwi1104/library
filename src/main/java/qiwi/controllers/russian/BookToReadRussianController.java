package qiwi.controllers.russian;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.IO;
import qiwi.TimeFormat;
import qiwi.model.common.Input;
import qiwi.model.russian.BookToReadRussian;
import qiwi.model.russian.FinishedBookRussian;
import qiwi.service.russian.BookToReadServiceRussianImpl;
import qiwi.service.russian.FinishedBookServiceRussianImpl;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/bookstoread/russian")
public class BookToReadRussianController {
    @Autowired
    private BookToReadServiceRussianImpl service;
    @Autowired
    private FinishedBookServiceRussianImpl finishedBookService;

    private String sortDateMethod = "ASC";
    private String sortProperty = "found";

    private List<BookToReadRussian> filterAndSort() {
        List<BookToReadRussian> books = null;

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

    private List<BookToReadRussian> fillListWith(JSONArray source) {
        List<BookToReadRussian> destination = new ArrayList<>();

        for (int i = 0; i < source.length(); i++) {
            BookToReadRussian bookToAdd = new BookToReadRussian();

            bookToAdd.setId(i + 1);
            bookToAdd.setAuthor(source.getJSONObject(i).get("author").toString());
            bookToAdd.setName(source.getJSONObject(i).get("name").toString());

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d", source.getJSONObject(i).get("found").toString());

                bookToAdd.setFound(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setFound(Date.valueOf("1970-1-1"));
            }

            bookToAdd.setFoundDescription(source.getJSONObject(i).get("found_description").toString());


            destination.add(bookToAdd);
        }


        return destination;
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadRussianInput") Input input) {
        BookToReadRussian book = new BookToReadRussian();

        book.setId(service.findAll().size() + 1);
        book.setAuthor(input.getAuthor());
        book.setName(input.getName());
        book.setFound(input.getFound());
        book.setFoundDescription(input.getFoundDescription());

        service.addBook(book);

        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadRussianInput") Input input) {
        BookToReadRussian book = service.getBookById(input.getId());

        if (input.getAuthor().length() != 0) {
            book.setAuthor(input.getAuthor());
        }

        if (input.getName().length() != 0) {
            book.setName(input.getName());
        }

        if (input.getFound().toString().length() != 0) {
//            book.setFound(input.getFound()); пока что так
        }

        if (input.getFoundDescription().length() != 0) {
            book.setFoundDescription(input.getFoundDescription());
        }


        service.addBook(book);

        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);

        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/finish/{id}")
    public String finish(@ModelAttribute("booksToReadRussianInput") Input input) {
        BookToReadRussian bookToRead = service.getBookById(input.getId());

        FinishedBookRussian book = new FinishedBookRussian(bookToRead.getAuthor(), bookToRead.getName(), input.getStart(), input.getEnd(),
                "", "", bookToRead.getFound(), bookToRead.getFoundDescription());
        book.setId(finishedBookService.findAll().size() + 1);

        finishedBookService.addBook(book);
        service.deleteBook(bookToRead.getId());

        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        sortDateMethod = sortDateMethod.equals("ASC") ? "DESC" : "ASC";
        sortProperty = property;

        return "redirect:/bookstoread/russian/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("booksToReadRussianInput") Input input) throws IOException {
        service.clearAll();
        List<BookToReadRussian> bookToReadList;

        JSONArray jsonArray = IO.readJSONFile(input.getName());
        bookToReadList = fillListWith(jsonArray);

        service.addAll(bookToReadList);

        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/")
    public String list(Model model) {
        List<BookToReadRussian> bookList = filterAndSort();

        model.addAttribute("booksToRead", bookList);

        return "booksToReadRussian";
    }
}
