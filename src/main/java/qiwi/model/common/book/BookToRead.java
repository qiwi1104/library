package qiwi.model.common.book;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BookToRead extends Book implements Cloneable {
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        BookToRead book = (BookToRead) o;

        return name.equals(book.name) && author.equals(book.author);
    }

    @Override
    public BookToRead clone() {
        try {
            return (BookToRead) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return new BookToRead();
    }
}
