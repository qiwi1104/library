package qiwi.controllers.english;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.model.common.Input;
import qiwi.model.english.BookToRead;
import qiwi.model.english.FinishedBook;
import qiwi.service.english.BookToReadServiceImpl;
import qiwi.service.english.FinishedBookServiceImpl;

@Controller
@RequestMapping("/bookstoread/english")
public class BookToReadController {
    @Autowired
    private BookToReadServiceImpl service;
    @Autowired
    private FinishedBookServiceImpl finishedBookService;

    @PostMapping("/add")
    public String add(@ModelAttribute("booksToReadEnglishInput") Input input) {
        BookToRead book = new BookToRead();

        book.setId(service.findAll().size() + 1);
        book.setAuthor(input.getAuthor());
        book.setName(input.getName());
        book.setFound(input.getFound());
        book.setFoundDescription(input.getFoundDescription());

        service.addBook(book);

        return "redirect:/bookstoread/english/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("booksToReadEnglishInput") Input input) {
        BookToRead book = service.getBookById(input.getId());

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

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("booksToRead", service.findAll());

        return "booksToReadEnglish";
    }
}
