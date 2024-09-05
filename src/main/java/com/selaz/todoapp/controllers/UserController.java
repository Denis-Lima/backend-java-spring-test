package com.selaz.todoapp.controllers;

import com.selaz.todoapp.dtos.CreateUserDTO;
import com.selaz.todoapp.dtos.UpdateUserDTO;
import com.selaz.todoapp.entities.User;
import com.selaz.todoapp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "User")
@RequestMapping("users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    @Operation(summary = "List all users", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(description = "List of users", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = User.class))), responseCode = "200"),
    })
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a new user", responses = {
            @ApiResponse(description = "The created user", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class)), responseCode = "201"),
            @ApiResponse(description = "When some field is null or blank, or 'nivel' is different of 'admin' or 'user'", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE), responseCode = "400")
    })
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) throws ValidationException {
        User createdUser = userService.createUser(createUserDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update the user with given {id}", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(description = "The updated user", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class)), responseCode = "200"),
            @ApiResponse(description = "When some field is blank, or if 'nivel' is provided and is different of 'admin' or 'user'", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE), responseCode = "400")
    })
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO updateUserDTO) throws ValidationException {
        User updatedUser = userService.updateUser(id, updateUserDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the user with given {id}", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
