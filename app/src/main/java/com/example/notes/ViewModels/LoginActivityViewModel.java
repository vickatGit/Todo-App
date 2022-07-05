package com.example.notes.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.notes.Database.UserEntity;
import com.example.notes.Repository;

import java.util.List;

public class LoginActivityViewModel extends AndroidViewModel {
    private static final String TAG = "tag";
    public String username="";
    public String fullName="";
    public String password="";
    public String retypePassword="";
    public LiveData<List<UserEntity>> userEntity;
    Repository userRepository;


    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        userRepository=Repository.getInstance(application.getApplicationContext());
        userEntity=userRepository.getUser();
        Log.d(TAG, "LoginActivityViewModel: ");

    }

    public LiveData<List<UserEntity>>  getUser(){
        return userEntity;
    }

    public void addUser(UserEntity userEntity) {
        userRepository.addUser(userEntity);
    }
    public void deleteall(){
        userRepository.deleteAll();
    }
}
