package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "points_entries")
public class PointsEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private TaskInstance taskInstance;

    private int points;

    private LocalDateTime createdAt;

    public PointsEntry() {}

    public Long getId() { return id; }
    public User getUser() { return user; }
    public TaskInstance getTaskInstance() { return taskInstance; }
    public int getPoints() { return points; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setTaskInstance(TaskInstance taskInstance) { this.taskInstance = taskInstance; }
    public void setPoints(int points) { this.points = points; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}