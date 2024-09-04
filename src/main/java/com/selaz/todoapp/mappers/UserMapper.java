package com.selaz.todoapp.mappers;

import com.selaz.todoapp.dtos.CreateUserDTO;
import com.selaz.todoapp.dtos.UpdateUserDTO;
import com.selaz.todoapp.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void userFromCreateUserDTO(CreateUserDTO createUserDTO, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUpdateUserDTO(UpdateUserDTO updateUserDTO, @MappingTarget User user);
}
