package com.example.demo.controllers;

import com.example.demo.domain.Restaurant;
import com.example.demo.domain.Review;
import com.example.demo.storage.FirestoreStorage;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController()
public class RestaurantsController {
    private FirestoreStorage firestoreStorage = new FirestoreStorage();

    public RestaurantsController() throws IOException {
    }

    @PutMapping("/restaurants")
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) throws ExecutionException, InterruptedException {
        restaurant.id = UUID.randomUUID().toString();
        firestoreStorage.createRestaurant(restaurant);
        return restaurant;
    }

    @GetMapping("/restaurants")
    public List<Restaurant> listRestaurants() throws ExecutionException, InterruptedException {
        return firestoreStorage.listRestaurants();
    }

    @DeleteMapping("/restaurants/{restaurantId}")
    public void deleteRestaurant(@PathVariable String restaurantId) throws ExecutionException, InterruptedException {
        firestoreStorage.deleteRestaurant(restaurantId);
    }

    @PostMapping("/restaurants/{restaurantId}/reviews")
    public void createReview(@PathVariable String restaurantId, @RequestBody Review review) {
        review.id = UUID.randomUUID().toString();
        review.restaurantId = restaurantId;
        firestoreStorage.createReview(restaurantId, review);
    }

    @PostMapping("/restaurants")
    public List<Restaurant> createRestaurants(@RequestBody List<Restaurant> rs) throws ExecutionException, InterruptedException {
        for (var r : rs) {
            r.id = UUID.randomUUID().toString();
        }
        firestoreStorage.createRestaurants(rs);
        return rs;
    }
}
