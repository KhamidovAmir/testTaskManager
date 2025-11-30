package ru.tasksManager.TasksProject.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.tasksManager.TasksProject.Entity.Task;
import ru.tasksManager.TasksProject.Entity.TaskState;
import ru.tasksManager.TasksProject.dto.TaskDto;
import ru.tasksManager.TasksProject.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository repository;
    private TaskService service;

    @BeforeEach
    void setup(){
        repository = Mockito.mock(TaskRepository.class);
        service = new TaskService(repository);
    }

    @Test
    void testCreateTask(){
        TaskDto dto = new TaskDto("Test", "Desc", LocalDate.now());

        Task savedTask = new Task(1L, dto.getTitle(), dto.getDescription(), dto.getDeadline(), TaskState.TODO);
        when(repository.save(any(Task.class))).thenReturn(savedTask);

        Task result = service.createTask(dto);
        assertEquals("Test", result.getTitle());
        assertEquals(TaskState.TODO, result.getState());
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdateTask(){
        Task existing = new Task(1L, "Old", "OldDecs", LocalDate.now(), TaskState.TODO);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        TaskDto dto = new TaskDto("New", "NewDecs", LocalDate.now().plusDays(1));

        Optional<Task> updated = service.updateTask(1L, dto);
        assertTrue(updated.isPresent());
        assertEquals("New", updated.get().getTitle());

    }
    @Test
    void testSetDone(){
        Task task = new Task(1L, "Task", "Desc", LocalDate.now(), TaskState.TODO);
        when(repository.findById(1L)).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenReturn(task);

        boolean result = service.setDone(1L, true);
        assertTrue(result);
        assertEquals(TaskState.DONE, task.getState());
    }
    @Test
    void testDeleteTask(){
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        assertTrue(service.deleteTask(1L));
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testGetTasks(){
        LocalDate today = LocalDate.now();
        Task task1 = new Task(1L, "T1", "D1", today, TaskState.TODO);
        Task task2 = new Task(2L, "T2", "D2", today, TaskState.DONE);

        when(repository.findByDeadlineBetween(today,today)).thenReturn(List.of(task1, task2));
        List<Task> result = service.getTasks(today, today, TaskState.TODO);
        assertEquals(1, result.size());
        assertEquals(TaskState.TODO, result.get(0).getState());
    }
}
