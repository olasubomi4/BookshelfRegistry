package com.group5.bookshelfregistry.controller;

import com.group5.bookshelfregistry.dto.user.AuthUserRequest;
import com.group5.bookshelfregistry.entities.User;
import com.group5.bookshelfregistry.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@Validated
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/register")
    @Operation(summary = "This endpoint is used to register a user to the api")
    public ResponseEntity<HttpStatus> createUser(@Valid @RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Authenticate User and Generate Bearer Token",
            description = "Authenticate a user and generate a bearer token for authorization.Note auth token " +
                    "is in the response header")
    @PostMapping(value = "/authenticate")
    public void authenticate(@Valid @RequestBody AuthUserRequest userRequest) {
    }
}
