package com.sbsprotech.smart_task_tracker.task;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException(
                        "Task with id " + taskId + " does not exist"
                ));
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getStatus() == status)
                .toList();
    }

    public void addNewTask(Task task) {
        Optional<Task> taskByTitle = taskRepository.findByTitle(task.getTitle());
        if (taskByTitle.isPresent()) {
            throw new IllegalStateException("Title is already taken");
        }
        taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        boolean exists = taskRepository.existsById(taskId);
        if(!exists) {
            throw new IllegalStateException(
                    "task with id " + taskId + " doesn't exist"
            );
        }

        taskRepository.deleteById(taskId);
    }

    @Transactional
    public void updateTask(Long taskId, String title, String description, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()-> new IllegalStateException(
                        "task with id " + taskId + " doesn't exist"
                ));
        if (title != null && !title.isEmpty() && !Objects.equals(task.getTitle(), title)) {
            Optional<Task> taskOptional = taskRepository.findByTitle(title);
            if (taskOptional.isPresent()) {
                throw new IllegalStateException("Title is already taken");
            }
            task.setTitle(title);
        }

        if (description != null && !description.isEmpty() && !Objects.equals(task.getDescription(), description)) {
            task.setDescription(description);
        }

        if (status != null && !Objects.equals(task.getStatus(), status)) {
            task.setStatus(status);
        }
    }
}
