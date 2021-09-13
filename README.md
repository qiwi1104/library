# Library

An application to manage books that I want to read/have already read.

### Technologies

- Java 11
- Spring Boot
- Maven
- MySQL
- Thymeleaf

### Usage

Clone this repository
```
$ git clone https://github.com/qiwi1104/library
```
Create a MySQL database named `base` (or whatever you like, but don't forget to change its name in `application.properties` file) and add tables to it using `base_structure.sql` file (in `sample` folder).

Set up paths to back up folders in `config.properties` file (otherwise you will have to set the path to a file manually).

Go into the repository
```
$ cd library
```
Run the app
```
$ mvn spring-boot:run
```

Go to `localhost:8080/bookstoread/english/`

Load sample files (from `sample` folder) using `load` button.
#
P.S.

Front-end is obviously deficient, but I will get down to it sooner or later.