package qiwi.model.common.book;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BookToRead extends Book implements Cloneable {
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
