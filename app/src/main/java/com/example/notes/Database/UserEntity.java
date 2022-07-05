package com.example.notes.Database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "UserData")
public class UserEntity {

    @PrimaryKey
    int dataId=1;

    String user_name;
    String user_id;

    @Ignore
    public UserEntity() {
    }

    @Ignore
    public UserEntity(String user_name, String user_id) {
        this.user_name = user_name;
        this.user_id = user_id;
    }

    public UserEntity(int dataId, String user_name, String user_id) {
        this.dataId = dataId;
        this.user_name = user_name;
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
