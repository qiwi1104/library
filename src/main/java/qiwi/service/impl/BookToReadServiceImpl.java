//package qiwi.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import qiwi.model.impl.book.BookToRead;
//import qiwi.repository.impl.BookToReadRepository;
//import qiwi.service.BookService;
//
//import java.util.List;
//
//public abstract class BookToReadServiceImpl<
//        T extends BookToRead,
//        S extends BookToReadRepository<T>> implements BookService<T> {
//    @Autowired
//    private S repository;
//
//    public List<T> findAllByOrderByIdAsc() {
//        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
//    }
//
//    public List<T> findAllByOrderByIdDesc() {
//        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
//    }
//
//    public List<T> findAllByOrderByFoundByIdAsc() {
//        return repository.findAll(Sort.by(Sort.Direction.ASC, "found", "id"));
//    }
//
//    public List<T> findAllByOrderByFoundByIdDesc() {
//        return repository.findAll(Sort.by(Sort.Direction.DESC, "found", "id"));
//    }
//
//    @Override
//    public void addBook(T book) {
//        repository.save(book);
//    }
//
//    @Override
//    public void addAll(List<T> booksList) {
//        repository.saveAll(booksList);
//    }
//
//    @Override
//    public void deleteBook(Integer id) {
//        repository.deleteById(id);
//
//        // Deleted book was not the last one in the list
//        if (id != repository.findAll().size() + 1) {
//            // Computing new IDs for the books whose IDs were greater than that of the deleted book
//            repository.computeIds();
//        }
//    }
//
//    @Override
//    public void clearAll() {
//        repository.deleteAll();
//    }
//
//    @Override
//    public T getBookById(Integer id) {
//        if (id <= repository.count())
//            return repository.getOne(id);
//        else return null;
//    }
//
//    @Override
//    public boolean exists(T book) {
//        for (T bookToRead : repository.findAll()) {
//            if (bookToRead.equals(book)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    @Override
//    public List<T> findAll() {
//        return repository.findAll();
//    }
//}
