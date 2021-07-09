package qiwi.controllers.russian;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qiwi.IO;
import qiwi.TimeFormat;
import qiwi.controllers.CommonActions;
import qiwi.model.common.Input;
import qiwi.model.russian.AdditionalDatesRussian;
import qiwi.model.russian.FinishedBookRussian;
import qiwi.service.russian.AdditionalDatesServiceRussianImpl;
import qiwi.service.russian.FinishedBookServiceRussianImpl;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/finishedbooks/russian")
public class FinishedBookRussianController {
    @Autowired
    private FinishedBookServiceRussianImpl service;
    @Autowired
    private AdditionalDatesServiceRussianImpl additionalDatesService;

    private String sortDateMethod = "ASC";
    private String sortProperty = "start";

    private List<FinishedBookRussian> filterAndSort() {
        List<FinishedBookRussian> books = null;

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

    private List<FinishedBookRussian> fillList(JSONArray source) {
        List<FinishedBookRussian> destination = new ArrayList<>();

        for (int i = 0; i < source.length(); i++) {
            FinishedBookRussian bookToAdd = new FinishedBookRussian();

            bookToAdd.setId(i + 1);
            bookToAdd.setAuthor(source.getJSONObject(i).get("author").toString());
            bookToAdd.setName(source.getJSONObject(i).get("name").toString());

            boolean error = false;
            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                        source.getJSONObject(i).getJSONArray("dates").get(0).toString());

                bookToAdd.setStart(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setStartDescription(source.getJSONObject(i).getJSONArray("dates").get(0).toString()
                        + "\n" + source.getJSONObject(i).get("start_description").toString());

                bookToAdd.setStart(Date.valueOf("1970-1-1"));
                error = true;
            }

            try {
                String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                        source.getJSONObject(i).getJSONArray("dates").get(1).toString());

                bookToAdd.setEnd(Date.valueOf(date));
            } catch (Exception e) {
                bookToAdd.setEndDescription(source.getJSONObject(i).getJSONArray("dates").get(1).toString()
                        + "\n" + source.getJSONObject(i).get("end_description").toString());

                bookToAdd.setEnd(Date.valueOf("1970-1-1"));
                error = true;
            }

            if (!error) {
                bookToAdd.setStartDescription(source.getJSONObject(i).get("start_description").toString());
                bookToAdd.setEndDescription(source.getJSONObject(i).get("end_description").toString());
            }

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

    private void fillAdditionalDatesTable(JSONArray source) throws ParseException {
        for (int i = 0; i < source.length(); i++) {
            if (source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("start").length() != 0) {
                for (FinishedBookRussian finishedBook : service.findAll()) {
                    if (finishedBook.getName().equals(source.getJSONObject(i).get("name"))) {
                        AdditionalDatesRussian additionalDates = new AdditionalDatesRussian();

                        additionalDates.setId(additionalDatesService.findAll().size() + 1);
                        additionalDates.setFinishedBookId(finishedBook.getId());
                        for (int j = 0; j < source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("start").length(); j++) {
                            String date = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                                    source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("start").get(j).toString());

                            String date2 = TimeFormat.formatTime("M/d/yy", "yyyy-M-d",
                                    source.getJSONObject(i).getJSONObject("additional_dates").getJSONArray("end").get(j).toString());

                            additionalDates.setStart(Date.valueOf(date));
                            additionalDates.setEnd(Date.valueOf(date2));
                            additionalDatesService.addDates(additionalDates);
                        }
                    }
                }
            }
        }
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("finishedRussianInput") Input inputFinished) {
        FinishedBookRussian book = new FinishedBookRussian();

        book.setId(service.findAll().size() + 1);
        CommonActions.setBookAttributesFromInput(book, inputFinished, "addFirst");

        if (CommonActions.isInTable(book, service.findAll())) {
            additionalDatesService.addDates(new AdditionalDatesRussian(additionalDatesService.findAll().size() + 1,
                    book.getId(), book.getStart(), book.getEnd()));
        } else {
            CommonActions.setBookAttributesFromInput(book, inputFinished, "addSecond");

            service.addBook(book);
        }

        return "redirect:/finishedbooks/russian/";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("finishedRussianInput") Input inputFinished) {
        FinishedBookRussian book = service.getBookById(inputFinished.getId());

        CommonActions.setBookAttributesFromInput(book, inputFinished, "edit");

        service.addBook(book);

        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteBook(id);

        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/sort/{property}")
    public String sort(@PathVariable String property) {
        sortDateMethod = sortDateMethod.equals("ASC") ? "DESC" : "ASC";
        sortProperty = property;

        return "redirect:/finishedbooks/russian/";
    }

    @PostMapping("/load")
    public String load(@ModelAttribute("finishedRussianInput") Input input) throws IOException, ParseException {
        service.clearAll();
        List<FinishedBookRussian> finishedBooks;

        JSONArray jsonBooks = IO.readJSONFile(input.getName());
        finishedBooks = fillList(jsonBooks);

        service.addAll(finishedBooks);
        fillAdditionalDatesTable(jsonBooks); // добавляет доп. даты в свою таблицу

        return "redirect:/finishedbooks/russian/";
    }

    @GetMapping("/")
    public String list(Model model) {
        List<FinishedBookRussian> bookList = filterAndSort();

        model.addAttribute("books", bookList);
        model.addAttribute("additionalDates", additionalDatesService.findAll());

        return "finishedBooksRussian";
    }
}