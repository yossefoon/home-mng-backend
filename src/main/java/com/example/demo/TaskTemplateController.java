package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-templates")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskTemplateController {

    private final TaskTemplateRepository repo;

    public TaskTemplateController(TaskTemplateRepository repo) {
        this.repo = repo;
    }

    // GET /api/task-templates
    @GetMapping
    public List<TaskTemplate> all() {
        return repo.findAll();
    }

    // POST /api/task-templates
    @PostMapping
    public TaskTemplate create(@RequestBody TaskTemplate t) {
        // MVP: מינימום ולידציה
        if (t.getTitle() == null || t.getTitle().trim().isEmpty()) {
            throw new RuntimeException("title is required");
        }
        if (t.getRecurrenceType() == null || t.getRecurrenceType().trim().isEmpty()) {
            t.setRecurrenceType("DAILY");
        }
        if (t.getPoints() <= 0) {
            t.setPoints(10);
        }
        return repo.save(t);
    }

    // PUT /api/task-templates/{id}
    @PutMapping("/{id}")
    public TaskTemplate update(@PathVariable Long id, @RequestBody TaskTemplate input) {
        TaskTemplate existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        existing.setTitle(input.getTitle());
        existing.setPoints(input.getPoints());
        existing.setRecurrenceType(input.getRecurrenceType());
        existing.setRecurrenceDay(input.getRecurrenceDay());
        existing.setTimeOfDay(input.getTimeOfDay());

        return repo.save(existing);
    }

    // DELETE /api/task-templates/{id}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}