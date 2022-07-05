package com.example.notes.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notes.Database.UserEntity;
import com.example.notes.Repository;

import java.util.List;

public class SignupLoginViewModel extends AndroidViewModel {

    private final LiveData<List<UserEntity>> userEntity;
    Repository userRepository;

    public SignupLoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = Repository.getInstance(application.getApplicationContext());
        userEntity=userRepository.getUser();
    }
    public void addUser(UserEntity userEntity){
        userRepository.addUser(userEntity);
    }
    public LiveData<List<UserEntity>> getUser(){
        return userEntity;
    }
}
