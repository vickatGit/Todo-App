package com.example.notes.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserEntity.class},version = 4)
public abstract class UserDatabase extends RoomDatabase {

    public static volatile UserDatabase userDatabase;
    public static String DB_NAME="UserDataDatabase.db";

    public abstract UserDAO getUserDao();

    public static UserDatabase getInstance(Context context){
        if(userDatabase==null){
            synchronized (context){
                if(userDatabase==null){
                    userDatabase= Room.databaseBuilder(context.getApplicationContext(),UserDatabase.class,DB_NAME).fallbackToDestructiveMigration().build();
                }
            }
        }
        return userDatabase;
    }



}
