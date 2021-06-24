package qiwi.controllers.english;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.input.BookToReadInput;
import qiwi.model.english.FinishedBook;
import qiwi.model.english.BookToRead;
import qiwi.service.english.BookToReadServiceImpl;

import java.sql.Date;

@Controller
@RequestMapping("/bookstoread/english")
public class BookToReadController {
    @Autowired
    private BookToReadServiceImpl service;

    @PostMapping("/add")
    public String addBook(@ModelAttribute("input") BookToReadInput input, Model model) {
        BookToRead book = new BookToRead();

        book.setId(service.findAll().size() + 1);
        book.setAuthor(input.getAuthor());
        book.setName(input.getName());
        book.setFound(input.getFound());
        book.setFoundDescription(input.getFoundDescription());

        service.addBook(book);

        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);

        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/finish/{id}")
    public String finish(@PathVariable Integer id) {
        BookToRead bookToRead = service.getBookById(id);
//        FinishedBook book = new FinishedBook(bookToRead.getAuthor(), bookToRead.getName(), Date.valueOf("2021-1-1"), Date.valueOf("2021-1-1"),
//                "", "", Date.valueOf("2021-1-1"), "");

        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("booksToRead", service.findAll());

        return "booksToReadEnglish";
    }
}
