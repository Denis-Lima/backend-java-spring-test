package com.selaz.todoapp.dtos;

import com.selaz.todoapp.utils.constraints.NullOrNotBlank;

public record UpdateUserDTO(@NullOrNotBlank String username, @NullOrNotBlank String nivel,
                            @NullOrNotBlank String password) {
}
