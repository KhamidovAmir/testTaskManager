package ru.tasksManager.TasksProject.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.tasksManager.TasksProject.Entity.Task;
import ru.tasksManager.TasksProject.Entity.TaskState;
import ru.tasksManager.TasksProject.dto.TaskDto;
import ru.tasksManager.TasksProject.service.TaskService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testCreate() throws Exception {
        TaskDto dto = new TaskDto("Test", "Desc", LocalDate.now());

        Task returned = new Task(1L, "Test", "Desc", dto.getDeadline(), TaskState.TODO);
        Mockito.when(service.createTask(any(TaskDto.class))).thenReturn(returned);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test"))
                .andExpect(jsonPath("$.state").value("TODO"));
    }

}
