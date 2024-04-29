package com.group5.bookshelfregistry.repositories;

import com.group5.bookshelfregistry.entities.Book;
import com.group5.bookshelfregistry.entities.BookCategory;
import com.group5.bookshelfregistry.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface IBookRepository extends CrudRepository<Book,Long> {
    public Optional<Book> findFirstByTitle(String title);
    public Long countByCreatedBy(User user);
    public Long countByDeletedBy(User user);

    @Query("SELECT b FROM Book b " +
            "WHERE (:categoryId IS NULL OR b.bookCategory = :categoryId) " +
            "AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) " +
            "AND (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:isbn IS NULL OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :isbn, '%')))")
    public Page<Book> findByBookCategoryAndAuthorAndTitleAndIsbn(
            BookCategory categoryId,
            String author,
            String title,
            String isbn,
            Pageable pageable);
}

