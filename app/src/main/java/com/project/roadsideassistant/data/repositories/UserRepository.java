package com.project.roadsideassistant.data.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;

    public UserRepository() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
    }

    public void getUserDetails(){

        assert mAuth.getCurrentUser() != null;


    }


    public interface UserTasksListener{

    }

}
