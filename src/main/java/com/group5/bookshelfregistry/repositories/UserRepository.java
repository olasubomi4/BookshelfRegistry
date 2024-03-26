package com.group5.bookshelfregistry.repositories;



import com.group5.bookshelfregistry.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	 Optional<User> findByUsername(String username);
}