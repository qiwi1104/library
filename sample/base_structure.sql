-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Время создания: Сен 13 2021 г., 13:48
-- Версия сервера: 5.7.17
-- Версия PHP: 7.1.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `base`
--

-- --------------------------------------------------------

--
-- Структура таблицы `additional_dates_english`
--

CREATE TABLE `additional_dates_english` (
  `id` int(11) NOT NULL,
  `finished_book_id` int(11) NOT NULL,
  `start` date NOT NULL,
  `end` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Структура таблицы `additional_dates_russian`
--

CREATE TABLE `additional_dates_russian` (
  `id` int(11) NOT NULL,
  `finished_book_id` int(11) NOT NULL,
  `start` date NOT NULL,
  `end` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Структура таблицы `additional_dates_spanish`
--

CREATE TABLE `additional_dates_spanish` (
  `id` int(11) NOT NULL,
  `finished_book_id` int(11) NOT NULL,
  `start` date NOT NULL,
  `end` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Структура таблицы `books_to_read_english`
--

CREATE TABLE `books_to_read_english` (
  `id` int(11) NOT NULL,
  `author` text NOT NULL,
  `name` text NOT NULL,
  `found` date NOT NULL,
  `description` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `books_to_read_russian`
--

CREATE TABLE `books_to_read_russian` (
  `id` int(11) NOT NULL,
  `author` text NOT NULL,
  `name` text NOT NULL,
  `found` date NOT NULL,
  `description` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `books_to_read_spanish`
--

CREATE TABLE `books_to_read_spanish` (
  `id` int(11) NOT NULL,
  `author` text NOT NULL,
  `name` text NOT NULL,
  `found` date NOT NULL,
  `description` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `finished_books_english`
--

CREATE TABLE `finished_books_english` (
  `id` int(11) NOT NULL DEFAULT '0',
  `author` text NOT NULL,
  `name` text NOT NULL,
  `start` date NOT NULL,
  `end` date NOT NULL,
  `found` date NOT NULL,
  `description` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `finished_books_russian`
--

CREATE TABLE `finished_books_russian` (
  `id` int(11) NOT NULL DEFAULT '0',
  `author` text NOT NULL,
  `name` text NOT NULL,
  `start` date NOT NULL,
  `end` date NOT NULL,
  `found` date NOT NULL,
  `description` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `finished_books_spanish`
--

CREATE TABLE `finished_books_spanish` (
  `id` int(11) NOT NULL DEFAULT '0',
  `author` text NOT NULL,
  `name` text NOT NULL,
  `start` date NOT NULL,
  `end` date NOT NULL,
  `found` date NOT NULL,
  `description` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `additional_dates_english`
--
ALTER TABLE `additional_dates_english`
  ADD PRIMARY KEY (`id`),
  ADD KEY `additional_dates_english_ibfk_1` (`finished_book_id`) USING BTREE;

--
-- Индексы таблицы `additional_dates_russian`
--
ALTER TABLE `additional_dates_russian`
  ADD PRIMARY KEY (`id`),
  ADD KEY `additional_dates_russian_ibfk_1` (`finished_book_id`);

--
-- Индексы таблицы `additional_dates_spanish`
--
ALTER TABLE `additional_dates_spanish`
  ADD PRIMARY KEY (`id`),
  ADD KEY `additional_dates_english_ibfk_1` (`finished_book_id`) USING BTREE;

--
-- Индексы таблицы `books_to_read_english`
--
ALTER TABLE `books_to_read_english`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Индексы таблицы `books_to_read_russian`
--
ALTER TABLE `books_to_read_russian`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Индексы таблицы `books_to_read_spanish`
--
ALTER TABLE `books_to_read_spanish`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Индексы таблицы `finished_books_english`
--
ALTER TABLE `finished_books_english`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

--
-- Индексы таблицы `finished_books_russian`
--
ALTER TABLE `finished_books_russian`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

--
-- Индексы таблицы `finished_books_spanish`
--
ALTER TABLE `finished_books_spanish`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `additional_dates_english`
--
ALTER TABLE `additional_dates_english`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT для таблицы `additional_dates_russian`
--
ALTER TABLE `additional_dates_russian`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT для таблицы `additional_dates_spanish`
--
ALTER TABLE `additional_dates_spanish`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT для таблицы `books_to_read_english`
--
ALTER TABLE `books_to_read_english`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=177;
--
-- AUTO_INCREMENT для таблицы `books_to_read_russian`
--
ALTER TABLE `books_to_read_russian`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=238;
--
-- AUTO_INCREMENT для таблицы `books_to_read_spanish`
--
ALTER TABLE `books_to_read_spanish`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;
--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `additional_dates_english`
--
ALTER TABLE `additional_dates_english`
  ADD CONSTRAINT `additional_dates_english_ibfk_1` FOREIGN KEY (`finished_book_id`) REFERENCES `finished_books_english` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `additional_dates_russian`
--
ALTER TABLE `additional_dates_russian`
  ADD CONSTRAINT `additional_dates_russian_ibfk_1` FOREIGN KEY (`finished_book_id`) REFERENCES `finished_books_russian` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `additional_dates_spanish`
--
ALTER TABLE `additional_dates_spanish`
  ADD CONSTRAINT `additional_dates_spanish_ibfk_1` FOREIGN KEY (`finished_book_id`) REFERENCES `finished_books_spanish` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
