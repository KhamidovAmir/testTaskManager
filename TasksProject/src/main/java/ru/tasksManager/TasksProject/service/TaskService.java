package ru.tasksManager.TasksProject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tasksManager.TasksProject.Entity.Task;
import ru.tasksManager.TasksProject.Entity.TaskState;
import ru.tasksManager.TasksProject.dto.TaskDto;
import ru.tasksManager.TasksProject.repository.TaskRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public Task createTask(TaskDto dto){
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());
        task.setState(TaskState.TODO);
        return repository.save(task);
    }
    public Optional<Task> updateTask(Long id, TaskDto dto){
        return repository.findById(id).map(task -> {
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            task.setDeadline(dto.getDeadline());
            return repository.save(task);
        });
    }
    public boolean setDone(Long id,  boolean done){
        return repository.findById(id).map(task -> {
            task.setState(done ? TaskState.DONE : TaskState.TODO);
            repository.save(task);
            return true;
        }).orElse(false);
    }
    public boolean deleteTask(Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
            return true;
        }
        return false;
    }
    public List<Task> getTasks(LocalDate start, LocalDate end, TaskState state){
        List<Task> tasks = new ArrayList<>(repository.findByDeadlineBetween(start, end));
        if (state != null) {
            tasks.removeIf(task -> task.getState() != state);
        }
        return tasks;
    }

}
