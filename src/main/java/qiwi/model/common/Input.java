package qiwi.model.common;

import lombok.Data;

import java.sql.Date;

@Data
public class Input {
    private Integer id;
    private String author = "";
    private String name = "";
    private Date found = Date.valueOf("1970-1-1");
    private Date start = Date.valueOf("1970-1-1");
    private Date end = Date.valueOf("1970-1-1");
    private String description = "";
}
