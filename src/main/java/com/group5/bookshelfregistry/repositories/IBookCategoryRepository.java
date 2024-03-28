package com.group5.bookshelfregistry.repositories;

import com.group5.bookshelfregistry.entities.BookCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface IBookCategoryRepository extends CrudRepository< BookCategory,Long> {
    @Query("SELECT b FROM BookCategory b WHERE (:categoryName IS NULL OR b.name = :categoryName)")
    public Page<BookCategory> findAllByName(String categoryName, Pageable pageable);
}
