package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@RestController
@RequestMapping("/api/points")
public class PointsController {

    private final PointsEntryRepository pointsRepo;

    public PointsController(PointsEntryRepository pointsRepo) {
        this.pointsRepo = pointsRepo;
    }

    // GET /api/points/weekly
    @GetMapping("/weekly/details")
    public List<Map<String, Object>> weeklyDetails() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);

        LocalDateTime start = startOfWeek.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        List<PointsEntry> entries = pointsRepo.findByCreatedAtBetween(start, end);

        Map<Long, Map<String, Object>> byUser = new HashMap<>();

        for (PointsEntry e : entries) {
            Long userId = e.getUser().getId();

            byUser.putIfAbsent(userId, new HashMap<>());
            Map<String, Object> u = byUser.get(userId);

            u.put("userId", userId);
            u.put("userName", e.getUser().getName());

            int total = (int) u.getOrDefault("totalPoints", 0);
            u.put("totalPoints", total + e.getPoints());

            List<Map<String, Object>> list =
                    (List<Map<String, Object>>) u.getOrDefault("entries", new ArrayList<>());

            Map<String, Object> row = new HashMap<>();
            row.put("taskTitle", e.getTaskInstance().getTemplate().getTitle());
            row.put("points", e.getPoints());
            row.put("approvedAt", e.getCreatedAt().toString());

            list.add(row);
            u.put("entries", list);
        }

        return new ArrayList<>(byUser.values());
    }
}