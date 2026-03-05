package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskInstanceRepository extends JpaRepository<TaskInstance, Long> {

    List<TaskInstance> findByDate(LocalDate date);

    List<TaskInstance> findByDateAndAssignedUser_Id(LocalDate date, Long userId);

    List<TaskInstance> findByDateAndAssignedUserIsNull(LocalDate date);

    List<TaskInstance> findByStatus(String status);

    boolean existsByDateAndTemplate_Id(LocalDate date, Long templateId);

}