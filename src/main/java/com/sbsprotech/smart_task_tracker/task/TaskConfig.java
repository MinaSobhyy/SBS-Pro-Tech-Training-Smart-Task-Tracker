package com.sbsprotech.smart_task_tracker.task;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TaskConfig {

    @Bean
    CommandLineRunner commandLineRunner(TaskRepository repository) {
        return args -> {
            Task t1 = new Task(
                    "Setup development environment",
                    "Install Java, Postgres, and Spring Boot tools"
            );

            Task t2 = new Task(
                    "Create first API",
                    "Build and test CRUD API for Task entity"
            );

            repository.saveAll(
                    List.of(t1, t2)
            );
        };
    }
}
