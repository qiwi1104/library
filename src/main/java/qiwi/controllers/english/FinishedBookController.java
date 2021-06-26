package qiwi.controllers.english;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.controllers.input.FinishedBookInput;
import qiwi.model.english.FinishedBook;
import qiwi.service.english.AdditionalDatesServiceImpl;
import qiwi.service.english.FinishedBookServiceImpl;

@Controller
@RequestMapping("/finishedbooks/english")
public class FinishedBookController {
    @Autowired
    private FinishedBookServiceImpl service;
    @Autowired
    private AdditionalDatesServiceImpl additionalDatesService;

    @PostMapping("/add")
    public String add(@ModelAttribute("inputFinished") FinishedBookInput inputFinished) {
        FinishedBook book = new FinishedBook();

        book.setId(service.findAll().size() + 1);
        book.setAuthor(inputFinished.getAuthor());
        book.setName(inputFinished.getName());
        book.setStart(inputFinished.getStart());
        book.setEnd(inputFinished.getEnd());
        book.setStartDescription("");
        book.setEndDescription("");
        book.setFound(inputFinished.getFound());
        book.setFoundDescription("");

        service.addBook(book);

        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);

        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("books", service.findAll());
        model.addAttribute("additionalDates", additionalDatesService.findAll());

        return "finishedBooksEnglish";
    }
}