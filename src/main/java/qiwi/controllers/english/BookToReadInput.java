package qiwi.controllers.english;

import lombok.Data;

import java.sql.Date;

@Data
public class BookToReadInput {
    String author;
    String name;
    Date found;
    String foundDescription;
}
