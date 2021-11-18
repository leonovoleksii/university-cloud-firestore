package com.example.demo.controllers;

import com.example.demo.domain.Review;
import com.example.demo.domain.User;
import com.example.demo.storage.FirestoreStorage;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
public class UsersController {
    private FirestoreStorage firestoreStorage = new FirestoreStorage();

    public UsersController() throws IOException {
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        user.id = UUID.randomUUID().toString();
        firestoreStorage.createUser(user);
        return user;
    }

    @GetMapping("/users")
    public List<User> listUsers() throws ExecutionException, InterruptedException {
        return firestoreStorage.listUsers();
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable String userId) throws ExecutionException, InterruptedException {
        firestoreStorage.deleteUser(userId);
    }

    @GetMapping("/users/{userId}/reviews")
    public List<Review> getReviews(@PathVariable String userId) throws ExecutionException, InterruptedException {
        return firestoreStorage.getUserReviews(userId);
    }
}
