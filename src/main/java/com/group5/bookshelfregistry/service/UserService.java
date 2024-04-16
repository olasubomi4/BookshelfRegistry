package com.group5.bookshelfregistry.service;


import com.group5.bookshelfregistry.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {
    User getUser(String username);
    User saveUser(User user);

    Optional<User> getCurrentlyLoggedInUsername();

    ResponseEntity<?> report();

}