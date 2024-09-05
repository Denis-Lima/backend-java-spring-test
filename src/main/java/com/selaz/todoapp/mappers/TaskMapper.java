package com.selaz.todoapp.mappers;

import com.selaz.todoapp.dtos.CreateTaskDTO;
import com.selaz.todoapp.dtos.UpdateTaskDTO;
import com.selaz.todoapp.entities.Task;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void taskFromCreateTaskDTO(CreateTaskDTO createTaskDTO, @MappingTarget Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromUpdateTaskDTO(UpdateTaskDTO updateTaskDTO, @MappingTarget Task task);
}
