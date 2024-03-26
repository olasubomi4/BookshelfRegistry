package com.group5.bookshelfregistry.service;


import com.group5.bookshelfregistry.entities.User;

public interface UserService {
    User getUser(String username);
    User saveUser(User user);

}