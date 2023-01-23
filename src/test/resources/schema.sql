CREATE TABLE books_to_read (
  id int NOT NULL AUTO_INCREMENT,
  author text NOT NULL,
  name text NOT NULL,
  found date DEFAULT NULL,
  description text NOT NULL,
  language text NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE finished_books (
  id int NOT NULL AUTO_INCREMENT,
  author text NOT NULL,
  name text NOT NULL,
  start date NOT NULL,
  end date NOT NULL,
  found date NOT NULL,
  description text NOT NULL,
  language text NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE additional_dates (
  id int NOT NULL AUTO_INCREMENT,
  finished_book_id int DEFAULT NULL,
  start date NOT NULL,
  end date NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (finished_book_id) REFERENCES finished_books (id) ON DELETE CASCADE ON UPDATE CASCADE
);