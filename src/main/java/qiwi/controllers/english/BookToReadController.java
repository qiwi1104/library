package qiwi.controllers.english;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.model.english.FinishedBook;
import qiwi.model.english.BookToRead;
import qiwi.service.BookToReadEnglishSerivceImpl;

import java.sql.Date;

@Controller
@RequestMapping("/bookstoread/english")
public class BookToReadController {
    @Autowired
    private BookToReadEnglishSerivceImpl service;

    @PostMapping("/add")
//    public String addBook(@RequestParam(defaultValue = "") String author, @RequestParam String name,
//                          @RequestParam Date found, @RequestParam(defaultValue = "") String foundDescription) {
    public String addBook(@ModelAttribute("input") BookToReadInput input, Model model) {
        BookToRead book = new BookToRead();
        book.setAuthor(input.author);
        book.setName(input.name);
        book.setFound(input.found);
        book.setFoundDescription(input.foundDescription);
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
        FinishedBook book = new FinishedBook(bookToRead.getAuthor(), bookToRead.getName(), Date.valueOf("2021-1-1"), Date.valueOf("2021-1-1"),
                "", "", Date.valueOf("2021-1-1"), "");

        return "redirect:/bookstoread/english/";
    }

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("booksToRead", service.findAll());

        return "index2";
    }
}
