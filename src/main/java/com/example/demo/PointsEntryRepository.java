package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface PointsEntryRepository extends JpaRepository<PointsEntry, Long> {

    List<PointsEntry> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}