package qiwi.controllers.english;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.IO;
import qiwi.TimeFormat;
import qiwi.model.english.AdditionalDates;
import qiwi.model.english.BookToRead;
import qiwi.model.english.FinishedBook;
import qiwi.model.common.Input;
import qiwi.service.english.AdditionalDatesServiceImpl;
import qiwi.service.english.FinishedBookServiceImpl;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/finishedbooks/english")
public class FinishedBookController {
    @Autowired
    private FinishedBookServiceImpl service;
    @Autowired
    private AdditionalDatesServiceImpl additionalDatesService;

    private String sortDateMethod = "ASC";
    private String sortProperty = "start";

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

    private List<FinishedBook> filterAndSort() {
        List<FinishedBook> books = null;

        switch (sortDateMethod) {
            case "ASC":
                switch (sortProperty) {
                    case "id":
                        books = service.findAllByOrderByIdAsc();
                        break;
                    case "start":
                        books = service.findAllByOrderByStartAsc();
                        break;
                    case "end":
                        books = service.findAllByOrderByEndAsc();
                        break;
                }
                break;
            case "DESC":
                switch (sortProperty) {
                    case "id":
                        books = service.findAllByOrderByIdDesc();
                        break;
                    case "start":
                        books = service.findAllByOrderByStartDesc();
                        break;
                    case "end":
                        books = service.findAllByOrderByEndDesc();
                        break;
                }
                break;
        }
        return books;
    }

    private List<FinishedBook> fillListWith(JSONArray source) throws ParseException {
        List<FinishedBook> destination = new ArrayList<>();

        for (int i = 0; i < source.length(); i++) {
            FinishedBook bookToAdd = new FinishedBook();

            bookToAdd.setId(service.findAll().size() + 1);
            bookToAdd.setAuthor(source.getJSONObject(i).get("author").toString());
            bookToAdd.setName(source.getJSONObject(i).get("name").toString());

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                        source.getJSONObject(i).getJSONArray("dates").get(0).toString());
                bookToAdd.setStart(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setStart(Date.valueOf("1970-1-1"));
            }

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                        source.getJSONObject(i).getJSONArray("dates").get(1).toString());
                bookToAdd.setEnd(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setEnd(Date.valueOf("1970-1-1"));
            }

            bookToAdd.setStartDescription(source.getJSONObject(i).get("start_description").toString());
            bookToAdd.setEndDescription(source.getJSONObject(i).get("end_description").toString());

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d", source.getJSONObject(i).get("found").toString());
                bookToAdd.setFound(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setFound(Date.valueOf("1970-1-1"));
            }

            bookToAdd.setFoundDescription(source.getJSONObject(i).get("found_description").toString());

//            if (source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("start").length() != 0) {
//                AdditionalDates additionalDates = new AdditionalDates();
//
//                additionalDates.setId(additionalDatesService.findAll().size() + 1);
//                additionalDates.setFinishedBookId(bookToAdd.getId());
//                for (int j = 0; j < source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("start").length(); j++) {
//                    String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
//                            source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("start").get(j).toString());
//
//                    String date2 = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
//                            source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("end").get(j).toString());
//
//                    additionalDates.setStart(Date.valueOf(date));
//                    additionalDates.setEnd(Date.valueOf(date2));
//                    additionalDatesService.addDates(additionalDates);
//                }
//
//            }

            destination.add(bookToAdd);
        }


        return destination;
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

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        sortDateMethod = sortDateMethod.equals("ASC") ? "DESC" : "ASC";
        sortProperty = property;

        return "redirect:/finishedbooks/english/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("finishedEnglishInput") Input input) throws IOException, ParseException {
        service.clearAll();
        List<FinishedBook> bookToReadList;

        JSONArray jsonArray = IO.readJSONFile(input.getName());
        bookToReadList = fillListWith(jsonArray);

        service.addAll(bookToReadList);

        return "redirect:/finishedbooks/english/";
    }

    @GetMapping("/")
    public String list(Model model) {
        List<FinishedBook> bookList = filterAndSort();

        model.addAttribute("books", bookList);
        model.addAttribute("additionalDates", additionalDatesService.findAll());

        return "finishedBooksEnglish";
    }
}