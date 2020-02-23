package com.project.roadsideassistant.data.repositories;

import com.google.firebase.auth.FirebaseAuth;

public class UserRepository {

    private FirebaseAuth mAuth;

    public UserRepository() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void getUserDetails(){

    }


    public interface UserTasksListener{

    }

}
