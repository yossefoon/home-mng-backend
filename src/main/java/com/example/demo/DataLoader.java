package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalTime;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TaskTemplateRepository taskTemplateRepository;

    public DataLoader(UserRepository userRepository,
                      TaskTemplateRepository taskTemplateRepository) {
        this.userRepository = userRepository;
        this.taskTemplateRepository = taskTemplateRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User mom = new User("אמא", "PARENT");
            User dad = new User("אבא", "PARENT");
            User noa = new User("נועה", "CHILD");
            User yoav = new User("יואב", "CHILD");

            userRepository.saveAll(List.of(mom, dad, noa, yoav));
        }
        if (taskTemplateRepository.count() == 0) {
            TaskTemplate dishwasher = new TaskTemplate();
            dishwasher.setTitle("פינוי מדיח");
            dishwasher.setPoints(10);
            dishwasher.setRecurrenceType("DAILY");
            dishwasher.setTimeOfDay(java.time.LocalTime.of(19, 0));

            taskTemplateRepository.save(dishwasher);
        }

    }
}