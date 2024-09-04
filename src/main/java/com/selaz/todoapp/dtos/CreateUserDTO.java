package com.selaz.todoapp.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(@NotBlank String username, @NotBlank String nivel, @NotBlank String password) {
}
