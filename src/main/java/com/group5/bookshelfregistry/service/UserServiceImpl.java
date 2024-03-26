package com.group5.bookshelfregistry.service;


import com.group5.bookshelfregistry.entities.User;
import com.group5.bookshelfregistry.exceptions.EntityNotFoundException;
import com.group5.bookshelfregistry.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
//    private EntityUtility entityUtility;

    private BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder();
    private static Logger log = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return unwrapUser(user);
    }

    @Override
    public User saveUser(User user) {
    
        if (doesUserExist(user.getUsername())) {
//            log.info(ResponseMessages.USER_ALREADY_EXIST.getResponseMessage());
//            throw new DuplicateUserException(ResponseMessages.USER_ALREADY_EXIST.getResponseMessage());
        }
//        user.setId(entityUtility.generateSequence(User.SEQUENCE_NAME));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    static User unwrapUser(Optional<User> entity) {
        if (entity.isPresent()) return entity.get();
        else throw new EntityNotFoundException(User.class);
    }

    Boolean doesUserExist(String username)
    {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
