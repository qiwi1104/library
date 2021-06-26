package qiwi.controllers.english;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.model.english.AdditionalDates;
import qiwi.model.english.FinishedBook;
import qiwi.model.common.Input;
import qiwi.service.english.AdditionalDatesServiceImpl;
import qiwi.service.english.FinishedBookServiceImpl;

@Controller
@RequestMapping("/finishedbooks/english")
public class FinishedBookController {
    @Autowired
    private FinishedBookServiceImpl service;
    @Autowired
    private AdditionalDatesServiceImpl additionalDatesService;

    private boolean isInTable(FinishedBook book) {
        for (FinishedBook finishedBook : service.findAll()) {
            if (finishedBook.equals(book)) {
                book.setId(finishedBook.getId());
                book.setFound(finishedBook.getFound());
                return true;
            }
        }

        return false;
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("finishedEnglishInput") Input inputFinished) {
        FinishedBook book = new FinishedBook();

        book.setId(service.findAll().size() + 1);
        book.setAuthor(inputFinished.getAuthor());
        book.setName(inputFinished.getName());
        book.setStart(inputFinished.getStart());
        book.setEnd(inputFinished.getEnd());

        if (isInTable(book)) {
            additionalDatesService.addDates(new AdditionalDates(additionalDatesService.findAll().size() + 1,
                    book.getId(), book.getStart(), book.getEnd()));
        } else {
            book.setStartDescription("");
            book.setEndDescription("");
            book.setFound(inputFinished.getFound());
            book.setFoundDescription("");

            service.addBook(book);
        }

        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("finishedEnglishInput") Input inputFinished) {
        FinishedBook book = service.getBookById(inputFinished.getId());

        if (inputFinished.getAuthor().length() != 0) {
            book.setAuthor(inputFinished.getAuthor());
        }

        if (inputFinished.getName().length() != 0) {
            book.setName(inputFinished.getName());
        }

        if (inputFinished.getStart().toString().length() != 0) {
            book.setStart(inputFinished.getStart());
        }

        if (inputFinished.getEnd().toString().length() != 0) {
            book.setEnd(inputFinished.getEnd());
        }

        if (inputFinished.getFound().toString().length() != 0) {
//            book.setFound(inputFinished.getFound()); пока что так
        }

        if (inputFinished.getFoundDescription().length() != 0) {
            book.setFoundDescription(inputFinished.getFoundDescription());
        }


        service.addBook(book);

        return "redirect:/bookstoread/english/";
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