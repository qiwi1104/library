package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import qiwi.service.impl.BookToReadServiceImpl;
import qiwi.service.impl.FinishedBookServiceImpl;

public abstract class BookToReadController extends BookController {
    @Autowired
    protected BookToReadServiceImpl service;
    @Autowired
    protected FinishedBookServiceImpl finishedBookService;
}
