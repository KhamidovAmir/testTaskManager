package ru.tasksManager.TasksProject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tasksManager.TasksProject.Entity.Task;
import ru.tasksManager.TasksProject.Entity.TaskState;
import ru.tasksManager.TasksProject.dto.TaskDto;
import ru.tasksManager.TasksProject.service.TaskService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @PostMapping
    public Task create(@RequestBody TaskDto dto){
        return service.createTask(dto);
    }
    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody TaskDto dto){
        return service.updateTask(id,dto).orElseThrow(() -> new RuntimeException("Task not found"));
    }
    @PatchMapping("/{id}/done")
    public void setDone(@PathVariable Long id, @RequestParam boolean done){
        if (!service.setDone(id,done)) throw new RuntimeException("Task not found");
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        if (!service.deleteTask(id)) throw new RuntimeException("Task not found");
    }
    @GetMapping
    public List<Task> getTasks(@RequestParam LocalDate start,
                               @RequestParam LocalDate end,
                               @RequestParam(required = false) TaskState state){
        return service.getTasks(start, end, state);
    }
    @GetMapping("/today")
    public List<Task> today(@RequestParam(required = false) TaskState state){
        LocalDate today = LocalDate.now();
        return service.getTasks(today, today, state);
    }

    @GetMapping("/week")
    public List<Task> week(@RequestParam(required = false) TaskState state){
        LocalDate today = LocalDate.now();
        return service.getTasks(today, today.plusDays(6), state);
    }

    @GetMapping("/month")
    public List<Task> month(@RequestParam(required = false) TaskState state){
        LocalDate today = LocalDate.now();
        return service.getTasks(today, today.plusMonths(1).minusDays(1), state);
    }



}
