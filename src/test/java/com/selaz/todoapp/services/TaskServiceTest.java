package com.selaz.todoapp.services;

import com.selaz.todoapp.dtos.CreateTaskDTO;
import com.selaz.todoapp.dtos.UpdateTaskDTO;
import com.selaz.todoapp.entities.Status;
import com.selaz.todoapp.entities.Task;
import com.selaz.todoapp.entities.User;
import com.selaz.todoapp.exceptions.NotAllowedException;
import com.selaz.todoapp.exceptions.ResourceNotFoundException;
import com.selaz.todoapp.repositories.TaskRepository;
import com.selaz.todoapp.repositories.UserRepository;
import com.selaz.todoapp.services.impl.TaskServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TaskServiceTest {
    @Mock
    TaskRepository taskRepository;
    @Mock
    UserRepository userRepository;

    @Autowired
    @InjectMocks
    TaskServiceImpl taskService;
    User user1 = createTestUser(1L);
    User user2 = createTestUser(2L);

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given status to filter and sort field when getUserTasks, then return tasks")
    void Given_StatusToFilterAndSortField_When_GetUserTasks_Then_ReturnTasks() {
        Task task1 = createTestTask(1L, user1, Status.PENDENTE);
        Task task2 = createTestTask(2L, user1, Status.PENDENTE);

        List<Task> user1Tasks = List.of(task1, task2);

        Mockito.when(taskRepository.findByUser_IdAndStatus(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(user1Tasks);
        List<Task> tasks = taskService.getUserTasks(task1.getUser().getId(), Status.PENDENTE, "dueDate");

        Assertions.assertThat(tasks).isNotEmpty();
        Assertions.assertThat(tasks).size().isEqualTo(user1Tasks.size());
        Mockito.verify(taskRepository, Mockito.times(1))
                .findByUser_IdAndStatus(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Given no status to filter and no sort field when getUserTasks, then return tasks")
    void Given_NoStatusToFilterAndNoSortField_When_GetUserTasks_Then_ReturnTasks() {
        Task task1 = createTestTask(1L, user1, Status.PENDENTE);
        Task task2 = createTestTask(2L, user1, Status.PENDENTE);

        List<Task> user1Tasks = List.of(task1, task2);

        Mockito.when(taskRepository.findByUser_Id(Mockito.any(), Mockito.any()))
                .thenReturn(user1Tasks);
        List<Task> tasks = taskService.getUserTasks(task1.getUser().getId(), null, null);

        Assertions.assertThat(tasks).isNotEmpty();
        Assertions.assertThat(tasks).size().isEqualTo(user1Tasks.size());
        Mockito.verify(taskRepository, Mockito.times(1)).findByUser_Id(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Given correct info when createTask, then return created task")
    void Given_CorrectInfo_When_CreateTask_Then_ReturnCreatedTask() {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO("title", "description", new Date(), Status.PENDENTE);
        Task task = getTaskFromCreateTaskDTO(createTaskDTO, user1);

        Mockito.when(userRepository.getReferenceById(user1.getId())).thenReturn(user1);
        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(task);

        Task createdTask = taskService.createTask(createTaskDTO, user1.getId());

        Assertions.assertThat(createdTask).isNotNull();
        Assertions.assertThat(createdTask).isSameAs(task);
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Given invalid field when createTask, then throw ConstraintViolationException")
    void Given_InvalidField_When_CreateTask_Then_ThrowConstraintViolation() {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO("", "", new Date(), Status.PENDENTE);

        Assertions.assertThatThrownBy(() -> taskService.createTask(createTaskDTO, 1L))
                .isInstanceOf(ConstraintViolationException.class);
    }


    @Test
    @DisplayName("Given correct info when updateTask, then the updated task should be updated and returned")
    void Given_CorrectInfo_When_UpdateTask_Then_ReturnUpdatedTask() {
        Task task = createTestTask(1L, user1, Status.CONCLUIDA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO("new title", "new description", calendar.getTime(), Status.EM_ANDAMENTO);

        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(task);

        Task updatedTask = taskService.updateTask(updateTaskDTO, task.getId(), user1.getId());

        Assertions.assertThat(updatedTask).isNotNull();
        Assertions.assertThat(updatedTask.getId()).isEqualTo(task.getId());
        Assertions.assertThat(updatedTask.getTitle()).isEqualTo(updateTaskDTO.title());
        Assertions.assertThat(updatedTask.getDescription()).isEqualTo(updateTaskDTO.description());
        Assertions.assertThat(updatedTask.getDueDate()).isEqualTo(updateTaskDTO.dueDate());
        Assertions.assertThat(updatedTask.getStatus()).isEqualTo(updateTaskDTO.status());

        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Given not existing task when updateTask, then throw ResourceNotFoundException")
    void Given_NotExistingTask_When_UpdateTask_Then_ThrowResourceNotFoundException() {
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO("new title", "new description", new Date(), Status.EM_ANDAMENTO);

        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> taskService.updateTask(updateTaskDTO, 1L, 1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Given task from other user when updateTask, then throw NotAllowedException")
    void Given_TaskFromOtherUser_When_UpdateTask_Then_ThrowNotAllowedException() {
        Task task = createTestTask(1L, user1, Status.CONCLUIDA);
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO("new title", "new description", new Date(), Status.EM_ANDAMENTO);

        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Assertions.assertThatThrownBy(() -> taskService.updateTask(updateTaskDTO, task.getId(), task.getUser()
                        .getId() + 1L))
                .isInstanceOf(NotAllowedException.class);
    }

    @Test
    @DisplayName("Given existing task ID and is user task when deleteTask, then task is deleted")
    void Given_ExistingTaskIdAndIsUserTask_When_DeleteTask_Then_TaskIsDeleted() {
        Task task = createTestTask(1L, user1, Status.CONCLUIDA);
        Mockito.doNothing().when(taskRepository).deleteById(task.getId());
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.deleteTask(task.getId(), task.getUser().getId());
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById(task.getId());
    }

    @Test
    @DisplayName("Given existing task ID and is not user task when deleteTask, then throw NotAllowedException")
    void Given_ExistingTaskIdAndIsNotUserTask_When_DeleteTask_Then_ThrowNotAllowedException() {
        Task task = createTestTask(1L, user1, Status.CONCLUIDA);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Assertions.assertThatThrownBy(() -> taskService.deleteTask(task.getId(), task.getUser().getId() + 1L))
                .isInstanceOf(NotAllowedException.class);
    }


    @Test
    @DisplayName("Given not existing task ID when deleteTask, then do nothing")
    void Given_NotExistingTaskId_When_DeleteTask_Then_DoNothing() {
        Long taskId = 1L;
        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        taskService.deleteTask(taskId, 1L);
        Mockito.verify(taskRepository, Mockito.times(0)).deleteById(taskId);
    }

    private Task createTestTask(Long id, User user, Status status) {
        Task task = new Task();
        task.setId(id);
        task.setUser(user);
        task.setStatus(status);
        task.setTitle("title" + id);
        task.setDescription("description" + id);
        task.setDueDate(new Date());
        task.setCreatedAt(new Date());
        return task;
    }

    private User createTestUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setNivel("admin");
        user.setPassword("PASSWORD");
        user.setUsername("testUser" + id);
        return user;
    }

    private Task getTaskFromCreateTaskDTO(CreateTaskDTO createTaskDTO, User user) {
        Task task = new Task();
        task.setId(1L);
        task.setUser(user);
        task.setStatus(createTaskDTO.status());
        task.setTitle(createTaskDTO.title());
        task.setDescription(createTaskDTO.description());
        task.setDueDate(createTaskDTO.dueDate());
        task.setCreatedAt(new Date());
        return task;
    }
}
