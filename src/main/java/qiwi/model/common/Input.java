package qiwi.model.common;

import lombok.Data;

import java.sql.Date;

@Data
public class Input {
    private Integer id;
    private String author;
    private String name;
    private Date found;
    private Date start;
    private Date end;
    private String foundDescription;
}
