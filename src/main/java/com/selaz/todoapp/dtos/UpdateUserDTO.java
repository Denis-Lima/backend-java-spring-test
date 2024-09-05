package com.selaz.todoapp.dtos;

import com.selaz.todoapp.validators.NullOrNotBlank;

public record UpdateUserDTO(@NullOrNotBlank String username, @NullOrNotBlank String nivel,
                            @NullOrNotBlank String password) {
}
