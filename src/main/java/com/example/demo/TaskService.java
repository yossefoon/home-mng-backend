package com.example.demo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskTemplateRepository templateRepo;
    private final TaskInstanceRepository instanceRepo;
    private final PointsEntryRepository pointsRepo;

    public TaskService(TaskTemplateRepository templateRepo,
                       TaskInstanceRepository instanceRepo,
                       PointsEntryRepository pointsRepo) {
        this.templateRepo = templateRepo;
        this.instanceRepo = instanceRepo;
        this.pointsRepo = pointsRepo;
    }

    // משימות להיום עבור משתמש
    public List<TaskInstance> getTasksForToday(Long userId) {
        ensureTodayTasksExist();
        LocalDate today = LocalDate.now();

        // אם אין משימות להיום בכלל – ליצור לפי התבניות
        if (instanceRepo.findByDate(today).isEmpty()) {
            generateTasksForDate(today);
        }

        // משימות של המשתמש
        List<TaskInstance> mine =
                instanceRepo.findByDateAndAssignedUser_Id(today, userId);

        // משימות פתוחות (לא שויכו עדיין)
        List<TaskInstance> open =
                instanceRepo.findByDateAndAssignedUserIsNull(today);

        // מאחדים לרשימה אחת שנחזיר (mine + open)
        List<TaskInstance> result = new ArrayList<>();
        result.addAll(mine);
        result.addAll(open);
        return result;
    }

    // יצירת מופעי משימות לפי תבניות
    private void generateTasksForDate(LocalDate date) {
        List<TaskTemplate> templates = templateRepo.findAll();
        String dow = date.getDayOfWeek().name(); // MONDAY...

        for (TaskTemplate t : templates) {
            String type = t.getRecurrenceType();

            boolean shouldCreate =
                    "DAILY".equalsIgnoreCase(type) ||
                            ("WEEKLY".equalsIgnoreCase(type) && t.getRecurrenceDay() != null
                                    && t.getRecurrenceDay().equalsIgnoreCase(dow.substring(0,3))); // MON/TUE...

            if (shouldCreate) {
                TaskInstance instance = new TaskInstance();
                instance.setTemplate(t);
                instance.setDate(date);
                instance.setStatus("OPEN");
                instanceRepo.save(instance);
            }
        }
    }
    public void ensureTodayTasksExist() {
        LocalDate today = LocalDate.now();
        List<TaskTemplate> templates = templateRepo.findAll();

        String dow = today.getDayOfWeek().name(); // MONDAY...
        String dow3 = dow.substring(0,3);         // MON

        for (TaskTemplate t : templates) {
            boolean shouldCreate =
                    "DAILY".equalsIgnoreCase(t.getRecurrenceType()) ||
                            ("WEEKLY".equalsIgnoreCase(t.getRecurrenceType())
                                    && t.getRecurrenceDay() != null
                                    && t.getRecurrenceDay().equalsIgnoreCase(dow3));

            if (!shouldCreate) continue;

            boolean exists = instanceRepo.existsByDateAndTemplate_Id(today, t.getId());
            if (!exists) {
                TaskInstance inst = new TaskInstance();
                inst.setTemplate(t);
                inst.setDate(today);
                inst.setStatus("OPEN");
                instanceRepo.save(inst);
            }
        }
    }
    // ילד "לוקח" משימה
    public TaskInstance takeTask(Long taskId, User user) {
        TaskInstance instance = instanceRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (instance.getAssignedUser() == null
                && "OPEN".equalsIgnoreCase(instance.getStatus())) {
            instance.setAssignedUser(user);
            instance.setStatus("TAKEN");
            instanceRepo.save(instance);
        }

        return instance;
    }

    // ילד מסמן שסיים משימה
    public TaskInstance completeTask(Long taskId) {
        TaskInstance instance = instanceRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if ("TAKEN".equalsIgnoreCase(instance.getStatus())) {
            instance.setStatus("DONE");
            instanceRepo.save(instance);
        }

        return instance;
    }
    public List<TaskInstance> getPendingApprovals() {
        return instanceRepo.findByStatus("DONE");
    }

    public TaskInstance approveTask(Long taskId) {
        TaskInstance instance = instanceRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!"DONE".equalsIgnoreCase(instance.getStatus())) {
            return instance;
        }

        // חייב להיות משויך לילד
        if (instance.getAssignedUser() == null) {
            throw new RuntimeException("Task has no assigned user");
        }

        instance.setStatus("APPROVED");
        instanceRepo.save(instance);

        // יצירת נקודות
        PointsEntry entry = new PointsEntry();
        entry.setUser(instance.getAssignedUser());
        entry.setTaskInstance(instance);
        entry.setPoints(instance.getTemplate().getPoints());
        entry.setCreatedAt(java.time.LocalDateTime.now());
        pointsRepo.save(entry);

        return instance;
    }
}
