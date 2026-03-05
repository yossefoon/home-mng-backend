package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173") // נשנה לפי הפורט של React אחר כך
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // החזרת כל המשתמשים (בני המשפחה)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}