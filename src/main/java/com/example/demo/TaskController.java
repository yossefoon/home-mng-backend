package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService,
                          UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    // משימות להיום עבור משתמש
    // GET /api/tasks/today?userId=3
    @GetMapping("/today")
    public List<TaskInstance> getTodayTasks(@RequestParam Long userId) {
        return taskService.getTasksForToday(userId);
    }
    @GetMapping("/all-today")
    public List<TaskInstance> allToday() {
        return taskService.getTasksForToday(-1L); // רק כדי שייצר
    }
    // ילד לוקח משימה
    // POST /api/tasks/5/take?userId=3
    @PostMapping("/{id}/take")
    public TaskInstance takeTask(@PathVariable Long id, @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return taskService.takeTask(id, user);
    }
    @PostMapping("/today/refresh")
    public void refreshToday() {
        taskService.ensureTodayTasksExist();
    }
    // ילד מסמן שסיים משימה
    // POST /api/tasks/5/complete
    @PostMapping("/{id}/complete")
    public TaskInstance completeTask(@PathVariable Long id) {
        return taskService.completeTask(id);
    }
    // GET /api/tasks/pending-approvals
    @GetMapping("/pending-approvals")
    public List<TaskInstance> pendingApprovals() {
        return taskService.getPendingApprovals();
    }

    // POST /api/tasks/{id}/approve
    @PostMapping("/{id}/approve")
    public TaskInstance approve(@PathVariable Long id) {
        return taskService.approveTask(id);
    }
}