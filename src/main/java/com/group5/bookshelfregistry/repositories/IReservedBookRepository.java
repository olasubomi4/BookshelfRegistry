package com.group5.bookshelfregistry.repositories;

import com.group5.bookshelfregistry.entities.ReservedBook;
import com.group5.bookshelfregistry.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface IReservedBookRepository extends CrudRepository<ReservedBook,Long> {
    public Long countByUser(User user);
}
