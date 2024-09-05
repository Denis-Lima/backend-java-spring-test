package com.selaz.todoapp.controllers;

import com.selaz.todoapp.dtos.CreateTaskDTO;
import com.selaz.todoapp.dtos.UpdateTaskDTO;
import com.selaz.todoapp.entities.Status;
import com.selaz.todoapp.entities.Task;
import com.selaz.todoapp.entities.User;
import com.selaz.todoapp.services.TaskService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Task")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("tasks")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping
    @Operation(summary = "List all user tasks", responses = {
            @ApiResponse(description = "List of tasks", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Task.class))), responseCode = "200"),
    })
    public ResponseEntity<List<Task>> listAllUserTask(@RequestParam(name = "status", required = false) Status status, @RequestParam(name = "sort", required = false) SortField sort) {
        User authUser = getAuthenticatedUser();
        List<Task> userTasks = taskService.getUserTasks(authUser.getId(), status, sort.name());
        return new ResponseEntity<>(userTasks, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a new task", responses = {
            @ApiResponse(description = "The created task", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Task.class)), responseCode = "201"),
            @ApiResponse(description = "When some field is null or blank", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE), responseCode = "400")
    })
    public ResponseEntity<Task> createTask(@RequestBody @Valid CreateTaskDTO createTaskDTO) {
        User authUser = getAuthenticatedUser();
        Task createdTask = taskService.createTask(createTaskDTO, authUser.getId());
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update the task with given {id}", responses = {
            @ApiResponse(description = "The updated task", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Task.class)), responseCode = "200"),
            @ApiResponse(description = "When some provided field is blank", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE), responseCode = "400")
    })
    public ResponseEntity<Task> updateTask(@RequestBody @Valid UpdateTaskDTO updateTaskDTO, @PathVariable Long id) throws ValidationException {
        User authUser = getAuthenticatedUser();
        Task updatedTask = taskService.updateTask(updateTaskDTO, id, authUser.getId());
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the task with given {id}", responses = {
            @ApiResponse(description = "No return", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE), responseCode = "200"),
            @ApiResponse(description = "When try to delete a task that is not yours", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE), responseCode = "400")
    })
    public ResponseEntity<?> deleteTask(@PathVariable Long id) throws ValidationException {
        User authUser = getAuthenticatedUser();
        taskService.deleteTask(id, authUser.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public enum SortField {
        dueDate
    }
}
