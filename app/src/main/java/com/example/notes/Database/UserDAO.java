package com.example.notes.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity userEntity);

    @Query("SELECT * FROM UserData")
    LiveData<List<UserEntity>> getUserData();

    @Query("DELETE FROM UserData")
    int deleteAll();

}
