package com.group5.bookshelfregistry.repositories;

import com.group5.bookshelfregistry.entities.Book;
import com.group5.bookshelfregistry.entities.ReservedBook;
import com.group5.bookshelfregistry.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface IReservedBookRepository extends CrudRepository<ReservedBook,Long> {
    public Long countByUser(User user);
    public Page<ReservedBook> findAllByUser(User user, Pageable pageable);
    public ReservedBook findByUserAndBook(User user, Book book);
    public List<ReservedBook> findByUser(User user);


}
