package com.group5.bookshelfregistry.repositories;

import com.group5.bookshelfregistry.entities.Book;
import com.group5.bookshelfregistry.entities.ReadingProgress;
import com.group5.bookshelfregistry.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface IReadingProgressRepository extends CrudRepository<ReadingProgress,Long> {
    public Optional<ReadingProgress> findFirstByBookAndUser(Book book, User user);
    public Optional<Page<ReadingProgress>> findAllByBookAndUser(Book book, User user, Pageable pageable);

    public Optional<Page<ReadingProgress>> findAllByUser(User user,Pageable pageable);

}
