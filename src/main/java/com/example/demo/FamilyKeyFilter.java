package com.example.demo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class FamilyKeyFilter extends OncePerRequestFilter {

    private final String expectedKey;

    public FamilyKeyFilter(String expectedKey) {
        this.expectedKey = expectedKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // לא חוסמים preflight של CORS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();

        // מגנים רק על API
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // אם לא הוגדר מפתח ב-ENV, לא חוסמים (כדי לא להינעל בטעות)
        if (expectedKey == null || expectedKey.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String provided = request.getHeader("X-FAMILY-KEY");
        if (provided == null || !provided.equals(expectedKey)) {
            response.setStatus(401);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write("{\"error\":\"Unauthorized\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}