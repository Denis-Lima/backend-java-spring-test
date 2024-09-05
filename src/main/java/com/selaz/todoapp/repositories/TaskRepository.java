package com.selaz.todoapp.repositories;

import com.selaz.todoapp.entities.Status;
import com.selaz.todoapp.entities.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser_Id(Long id, Sort sort);

    List<Task> findByUser_IdAndStatus(Long id, Status status, Sort sort);
}