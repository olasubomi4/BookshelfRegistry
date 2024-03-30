package com.group5.bookshelfregistry.security.utill;

import com.group5.bookshelfregistry.entities.User;
import com.group5.bookshelfregistry.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class SecurityUtility {

    private UserRepository userRepository;

    public Optional<User> getCurrentlyLoggedInUsername() {
    Authentication authentication = Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication).orElse(null);
    String username= (authentication != null && authentication.getPrincipal() != null)?authentication.getName()
       :null;
    return userRepository.findByUsername(username);

    }
}
