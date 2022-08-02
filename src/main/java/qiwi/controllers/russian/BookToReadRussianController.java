package qiwi.controllers.russian;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.common.BookToReadController;
import qiwi.dto.PathDTO;
import qiwi.model.book.BookToRead;
import qiwi.model.book.FinishedBook;
import qiwi.model.enums.Language;
import qiwi.model.enums.SortBy;

import static qiwi.model.enums.Language.RUSSIAN;

@Controller
@RequestMapping("/bookstoread/russian")
@SessionAttributes(types = Integer.class, names = "id")
public class BookToReadRussianController extends BookToReadController {
    private final Language language = RUSSIAN;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("book", new BookToRead());

        return "books-to-read/russian/add-book";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("book") BookToRead book, BindingResult result, Model model) {
        return service.addBook(book, result, model, language);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("book", service.getBookById(id));

        return "books-to-read/russian/edit-book";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("book") BookToRead book, BindingResult result, Model model) {
        return service.editBook(book, result, model, language);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBookById(id);
        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/finish/{id}")
    public String finish(@PathVariable Integer id, Model model) {
        BookToRead book = service.getBookById(id);

        model.addAttribute("book", new FinishedBook(book));
        model.addAttribute("id", book.getId());

        return "books-to-read/russian/finish-book";
    }

    @PostMapping("/finish")
    public String finish(@ModelAttribute("book") FinishedBook book, @SessionAttribute("id") Integer id) {
        finishedBookService.addBook(book);

        service.deleteBookById(id);

        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        service.sort(SortBy.valueOf(property.toUpperCase()));
        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/load")
    public String load(Model model) {
        model.addAttribute("path", new PathDTO());
        return "books-to-read/russian/load";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("path") PathDTO input) {
        service.load(input, language);
        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/loadBatch")
    public String loadBatch(Model model) {
        model.addAttribute("path", new PathDTO());
        return "books-to-read/russian/load-batch";
    }

    @PostMapping("/loadBatch")
    public String loadBatch(@ModelAttribute("path") PathDTO input) {
        service.loadBatch(input, language);
        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/save")
    public String save(Model model) {
        model.addAttribute("path", new PathDTO());
        return "books-to-read/russian/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("path") PathDTO input) {
        service.save(input, language);
        return "redirect:/bookstoread/russian/";
    }

    @GetMapping("/")
    public String showAllBooks(Model model) {
        service.setUpView(model, language);
        return "books-to-read/" + language.firstLetterToUpperCase() + "/index";
    }
}