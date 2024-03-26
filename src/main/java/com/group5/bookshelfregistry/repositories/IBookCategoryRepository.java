package com.group5.bookshelfregistry.repositories;

import com.group5.bookshelfregistry.entities.BookCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface IBookCategoryRepository extends CrudRepository< BookCategory,Long> {
}
