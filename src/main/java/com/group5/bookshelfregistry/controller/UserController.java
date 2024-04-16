package com.group5.bookshelfregistry.controller;

import com.group5.bookshelfregistry.dto.BaseResponse;
import com.group5.bookshelfregistry.dto.user.AuthUserRequest;
import com.group5.bookshelfregistry.entities.User;
import com.group5.bookshelfregistry.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import static com.group5.bookshelfregistry.enums.ResponseDefinition.*;

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

    @Operation(summary = "Get user information",
            description = "This endpoint show the user name and role")
    @PreAuthorize("hasAnyRole('ADMIN','VIEWER')")
    @GetMapping("")
    public ResponseEntity<?> getUserInfo() {
       User user= userService.getCurrentlyLoggedInUsername().orElseThrow(() -> new NotFoundException(FAILED_TO_RETRIEVED_USER.getMessage()));
       BaseResponse baseResponse= BaseResponse.builder().success(SUCCESSFULLY_RETRIEVED_USER.getSuccessful()).message(
                SUCCESSFULLY_RETRIEVED_USER.getMessage()).data(user).build();
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }
    @Operation(summary = "Get user report",
            description = "This endpoint shows the number of books reserved by a user, if the user is an admin it will" +
                    " display the number of books created by the user")
    @PreAuthorize("hasAnyRole('ADMIN','VIEWER')")
    @GetMapping(value ="report")
    public ResponseEntity<?> Admin () {
        return userService.report();
    }


}
