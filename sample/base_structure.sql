DROP TABLE IF EXISTS `books_to_read`;
CREATE TABLE `books_to_read` (
  `id` int NOT NULL AUTO_INCREMENT,
  `author` text,
  `name` text,
  `found` date DEFAULT NULL,
  `description` text,
  `language` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `finished_books`;
CREATE TABLE `finished_books` (
  `id` int NOT NULL AUTO_INCREMENT,
  `author` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` text NOT NULL,
  `start` date NOT NULL,
  `end` date NOT NULL,
  `found` date NOT NULL,
  `description` text NOT NULL,
  `language` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `additional_dates`;
CREATE TABLE `additional_dates` (
  `id` int NOT NULL AUTO_INCREMENT,
  `finished_book_id` int DEFAULT NULL,
  `start` date NOT NULL,
  `end` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `finished_book_id_idx` (`finished_book_id`),
  CONSTRAINT `finished_book_id` FOREIGN KEY (`finished_book_id`) REFERENCES `finished_books` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;