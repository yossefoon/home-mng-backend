package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "task_instances")
public class TaskInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private TaskTemplate template;

    private LocalDate date;

    @ManyToOne
    private User assignedUser; // null = משימה פתוחה

    // OPEN / TAKEN / DONE / APPROVED
    private String status;

    public TaskInstance() {}

    public Long getId() { return id; }
    public TaskTemplate getTemplate() { return template; }
    public LocalDate getDate() { return date; }
    public User getAssignedUser() { return assignedUser; }
    public String getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setTemplate(TaskTemplate template) { this.template = template; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setAssignedUser(User assignedUser) { this.assignedUser = assignedUser; }
    public void setStatus(String status) { this.status = status; }
}