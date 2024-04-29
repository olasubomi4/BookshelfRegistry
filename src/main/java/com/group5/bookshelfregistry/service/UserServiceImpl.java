package com.group5.bookshelfregistry.service;


import com.group5.bookshelfregistry.dto.BaseResponse;
import com.group5.bookshelfregistry.dto.report.ReportResponse;
import com.group5.bookshelfregistry.entities.User;
import com.group5.bookshelfregistry.enums.Roles;
import com.group5.bookshelfregistry.exceptions.DuplicateUserException;
import com.group5.bookshelfregistry.exceptions.EntityNotFoundException;
import com.group5.bookshelfregistry.repositories.IBookRepository;
import com.group5.bookshelfregistry.repositories.IReservedBookRepository;
import com.group5.bookshelfregistry.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

import static com.group5.bookshelfregistry.enums.ResponseDefinition.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder();
    private static Logger log = LogManager.getLogger(UserServiceImpl.class);
    @Autowired
    IBookRepository iBookRepository;
    @Autowired
    IReservedBookRepository iReservedBookRepository;


    @Override
    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return unwrapUser(user);
    }

    @Override
    public User saveUser(User user) {
    
        if (doesUserExist(user.getUsername())) {
            log.info(USER_ALREADY_EXIST.getMessage());
            throw new DuplicateUserException(USER_ALREADY_EXIST.getMessage());
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getCurrentlyLoggedInUsername() {
        Authentication authentication = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication).orElse(null);
        String username= (authentication != null && authentication.getPrincipal() != null)?authentication.getName()
                :null;
        try {
            return Optional.ofNullable(getUser(username));
        }
        catch (EntityNotFoundException entityNotFoundException) {
            return Optional.ofNullable(null);
        }
    }


    @Override
    public ResponseEntity<?> report() {
        User user= getCurrentlyLoggedInUsername().orElseThrow(() -> new NotFoundException(FAILED_TO_RETRIEVED_USER.getMessage()));
        ReportResponse reportResponse;
        if(Roles.ADMIN.getRoleName().equalsIgnoreCase(user.getRole())) {
            Long numberOfBooksCreatedByUser=iBookRepository.countByCreatedBy(user);
            Long numberOfBooksDeletedByUser= iBookRepository.countByDeletedBy(user);
            Long numberOfBooksReservedByUser=iReservedBookRepository.countByUser(user);
            reportResponse= ReportResponse.builder().numberOfBooksCreatedByUser(numberOfBooksCreatedByUser).numberOfBooksReservedByUser(numberOfBooksReservedByUser)
                    .numberOfBookDeletedByUser(numberOfBooksDeletedByUser).build();
        }
        else{
            Long numberOfBooksReservedByUser=iReservedBookRepository.countByUser(user);
            reportResponse=ReportResponse.builder().numberOfBooksReservedByUser(numberOfBooksReservedByUser).build();
        }
        BaseResponse baseResponse= BaseResponse.builder().message(SUCCESSFUL.getMessage())
                .success(SUCCESSFUL.getSuccessful()).data(reportResponse).build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    static User unwrapUser(Optional<User> entity) {
        if (entity.isPresent()) return entity.get();
        else throw new EntityNotFoundException(User.class);
    }

    Boolean doesUserExist(String username)
    {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            return true;
        }
        else {
            return false;
        }
    }
}
