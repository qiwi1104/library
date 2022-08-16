package qiwi.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import qiwi.service.dao.BookToReadDAO;
import qiwi.service.service.BookToReadService;

public abstract class BookToReadController extends BookController {
    @Autowired
    protected BookToReadService bookToReadService;
    @Autowired
    protected BookToReadDAO bookToReadDAO;
}
