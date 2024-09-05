package com.selaz.todoapp.dtos;

import com.selaz.todoapp.entities.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record CreateTaskDTO(@NotBlank String title, @NotBlank String description, @NotNull Date dueDate,
                            @NotNull Status status) {
}
