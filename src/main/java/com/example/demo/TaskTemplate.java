package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "task_templates")
public class TaskTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private int points;

    private String recurrenceType;  // "DAILY" / "WEEKLY" / "NONE"
    private String recurrenceDay;   // למשל "MON" אם WEEKLY

    private LocalTime timeOfDay;

    public TaskTemplate() {}

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public int getPoints() { return points; }
    public String getRecurrenceType() { return recurrenceType; }
    public String getRecurrenceDay() { return recurrenceDay; }
    public LocalTime getTimeOfDay() { return timeOfDay; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setPoints(int points) { this.points = points; }
    public void setRecurrenceType(String recurrenceType) { this.recurrenceType = recurrenceType; }
    public void setRecurrenceDay(String recurrenceDay) { this.recurrenceDay = recurrenceDay; }
    public void setTimeOfDay(LocalTime timeOfDay) { this.timeOfDay = timeOfDay; }
}