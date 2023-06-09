package com.example.xadrez.Persistance;


import com.example.xadrez.dto.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class UserPersistance {

    public static void register(String email, String password, String username) throws PersistanceException {
        AtomicReference<PersistanceException> atomicExeption = new AtomicReference<>(null);

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(username).build());

                    // Adiciona o campo "username" ao documento do usuário no Firestore
                    DocumentReference userDocRef = db.collection("users").document(user.getUid());
                    Map<String, Object> userDocData = new HashMap<>();
                    userDocData.put("username", username);
                    userDocRef.set(userDocData)
                            .addOnFailureListener(e -> {
                                user.delete();
                                atomicExeption.set(new PersistanceException(e.getMessage()));
                            });
                }).addOnFailureListener(e -> atomicExeption.set(new PersistanceException(e.getMessage())));

        if (atomicExeption.get() != null) {
            throw atomicExeption.get();
        }
    }

    public static void login(String email, String password) throws PersistanceException {
        AtomicReference<PersistanceException> atomicExeption = new AtomicReference<>(null);

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnFailureListener(e -> atomicExeption.set(new PersistanceException(e.getMessage())));

        if (atomicExeption.get() != null) {
            throw atomicExeption.get();
        }
    }

    public static User getUser() throws PersistanceException, NullPointerException {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AtomicReference<PersistanceException> atomicExeption = new AtomicReference<>(null);
        AtomicReference<User> atomicUser = new AtomicReference<>();

        if (user == null) {
            throw new NullPointerException("User is null. Try login");
        }

        DocumentReference userDocRef = db.collection("users").document(user.getUid());
        userDocRef.get()
                .addOnSuccessListener(documentSnapshot -> atomicUser.set(new User(documentSnapshot.getString("username"), documentSnapshot.getString("email"))))
                .addOnFailureListener(e -> atomicExeption.set(new PersistanceException(e.getMessage())));

        if (atomicExeption.get() != null) {
            throw atomicExeption.get();
        }
        return atomicUser.get();
    }
}
