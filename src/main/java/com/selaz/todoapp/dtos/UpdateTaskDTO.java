package com.selaz.todoapp.dtos;

import com.selaz.todoapp.entities.Status;
import com.selaz.todoapp.utils.constraints.NullOrNotBlank;

import java.util.Date;

public record UpdateTaskDTO(@NullOrNotBlank String title, @NullOrNotBlank String description, Date dueDate,
                            Status status) {
}
