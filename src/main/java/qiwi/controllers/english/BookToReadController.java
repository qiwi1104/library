package qiwi.controllers.english;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.input.BookToReadInput;
import qiwi.model.english.BookToRead;
import qiwi.service.english.BookToReadServiceImpl;

@Controller
@RequestMapping("/bookstoread/english")
public class BookToReadController {
    @Autowired
    private BookToReadServiceImpl service;

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadEnglishInput") BookToReadInput input) {
        BookToRead book = new BookToRead();

        book.setId(service.findAll().size() + 1);
        book.setAuthor(input.getAuthor());
        book.setName(input.getName());
        book.setFound(input.getFound());
        book.setFoundDescription(input.getFoundDescription());

        service.addBook(book);

        return "redirect:/bookstoread/english/";
    }
/*
    @GetMapping("/edit/{id}")
    public String edit() {
        BookToRead book = new BookToRead();

        if (input.getAuthor().length() != 0) {
            book.setAuthor(input.getAuthor());
        }
        if (input.getName().length() != 0) {
            book.setName(input.getName());
        }
        if (input.getFound().toString().length() != 0) {
            book.setFound(input.getFound());
        }
        if (input.getFoundDescription().length() != 0) {
            book.setFoundDescription(input.getFoundDescription());
        }

        return "redirect:/bookstoread/english/";
    }
*/
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);

        return "redirect:/bookstoread/english/";
    }
/*
    @PostMapping("/finish")
    public String finish(@ModelAttribute("finishBookEnglishInput") BookToReadInput input) {
        BookToRead bookToRead = service.getBookById(input.getId());
        service.deleteBook(bookToRead.getId());

        FinishedBook book = new FinishedBook(bookToRead.getAuthor(), bookToRead.getName(), input.getStart(), input.getEnd(),
                "", "", bookToRead.getFound(), bookToRead.getFoundDescription());


        return "redirect:/bookstoread/english/";
    }
*/
    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("booksToRead", service.findAll());

        return "booksToReadEnglish";
    }
}
