package com.example.demo.storage;

import com.example.demo.domain.Restaurant;
import com.example.demo.domain.Review;
import com.example.demo.domain.User;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class FirestoreStorage {
    private FirestoreOptions firestoreOptions =
            FirestoreOptions.newBuilder()
                .setProjectId("khmara")
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream("/Users/oleksiil/Downloads/khmara-2ccf225864d9.json")))
                .build();
    private Firestore db = firestoreOptions.getService();

    public FirestoreStorage() throws IOException {
    }

    public void createRestaurant(Restaurant r) throws ExecutionException, InterruptedException {
        var doc = db.collection("restaurants").document(r.id);
        HashMap<String, String> data = new HashMap<>();
        data.put("name", r.name);
        data.put("address", r.address);
        doc.set(data);
    }

    public List<Restaurant> listRestaurants() throws ExecutionException, InterruptedException {
        var q = db.collection("restaurants").get();
        List<Restaurant> rs = new LinkedList<>();

        for (var d : q.get().getDocuments()) {
            Restaurant r = new Restaurant();
            r.id = d.getId();
            r.address = d.getString("address");
            r.name = d.getString("name");
            rs.add(r);
        }

        return rs;
    }

    public void deleteRestaurant(String restaurantId) throws ExecutionException, InterruptedException {
        db.collection("restaurants").document(restaurantId).get().get().getReference().delete();
    }

    public void createUser(User user) {
        var doc = db.collection("users").document(user.id);
        HashMap<String, String> data = new HashMap<>();
        data.put("username", user.username);
        data.put("firstName", user.firstName);
        data.put("lastName", user.lastName);
        doc.set(data);
    }

    public List<User> listUsers() throws ExecutionException, InterruptedException {
        var q = db.collection("users").get();
        List<User> rs = new LinkedList<>();

        for (var d : q.get().getDocuments()) {
            User r = new User();
            r.id = d.getId();
            r.firstName = d.getString("firstName");
            r.lastName = d.getString("lastName");
            r.username = d.getString("username");
            rs.add(r);
        }

        return rs;
    }

    public void deleteUser(String userId) throws ExecutionException, InterruptedException {
        db.collection("users").document(userId).get().get().getReference().delete();
    }

    public List<Review> getUserReviews(String userId) throws ExecutionException, InterruptedException {
        var docs = db.collection("reviews").whereEqualTo("userId", userId).get().get().getDocuments();
        List<Review> rs = new LinkedList<>();
        for (var d : docs) {
            rs.add(fromDocument(d));
        }
        return rs;
    }

    public void createReview(String restaurantId, Review r) {
        var doc = db.collection("reviews").document(r.id);
        HashMap<String, String> data = new HashMap<>();
        data.put("restaurantId", r.restaurantId);
        data.put("text", r.text);
        data.put("userId", r.userId);
        doc.set(data);
    }

    public void createRestaurants(List<Restaurant> rs) throws ExecutionException, InterruptedException {
        db.collection("restaurants");

        var batch = db.batch();

        for (var r : rs) {
            var doc = db.collection("restaurants").document(r.id);
            HashMap<String, String> data = new HashMap<>();
            data.put("name", r.name);
            data.put("address", r.address);
            doc.set(data);
        }

        batch.commit();
    }

    private Review fromDocument(QueryDocumentSnapshot d) throws ExecutionException, InterruptedException {
        var r = new Review();
        r.id = d.getId();
        r.restaurantId = d.getString("restaurantId");
        r.userId = d.getString("userId");
        r.text = d.getString("text");
        r.username = d.getString("username");

        var user = db.collection("users").document(r.userId).get().get();
        r.username = user.getString("username");

        return r;
    }

}
