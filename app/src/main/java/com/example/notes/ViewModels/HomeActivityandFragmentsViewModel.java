package com.example.notes.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notes.Database.UserEntity;
import com.example.notes.HomeActivity;
import com.example.notes.Model.NoteModel;
import com.example.notes.Repository;

import java.util.List;

public class HomeActivityandFragmentsViewModel extends AndroidViewModel {

    private static final String TAG = "tag";
    Repository repository;
    private static String userId;
    LiveData<List<NoteModel>> allNotes;
    LiveData<List<NoteModel>> allTrashedNotes;
    private HomeActivity homeActivity;


    public HomeActivityandFragmentsViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
        allNotes=repository.getNotes(userId);
        allTrashedNotes=repository.getTrashedNotes();
        Log.d(TAG, "HomeActivityandFragmentsViewModel: "+userId);
    }
    public void addNote(NoteModel noteModel){
        Log.d(TAG, "addNote: ");
        noteModel.setReference(userId);
        repository.addNote(noteModel);
        allNotes=repository.getNotes(userId);

    }
    public LiveData<List<NoteModel>> getNotes(){
        return allNotes;
    }
    public LiveData<List<NoteModel>> getTrashNotes(){
        return allTrashedNotes;
    }

    public void fetchNotes(){
        allNotes=repository.getNotes(userId);
        allTrashedNotes=repository.getTrashedNotes();
    }
    public void deleteNote(String noteId){
        repository.deleteNote(noteId);
    }



    public static void setUserId(String UserId) {
        userId=UserId;
    }

    public void setParentActivity(HomeActivity homeActivity) {
        this.homeActivity=homeActivity;
    }
    public HomeActivity getParentActivity() {
        return homeActivity;
    }

    public void restoreNote(String noteId) {
        repository.restoreNote(noteId);
    }

    public void signOut() {
        repository.deleteUser();
    }

    public void setToolbartext(String title){
        homeActivity.setToolbartext(title);
    }

    public void setToolbarColor(boolean isColor){
        homeActivity.setToolbarColor(isColor);
    }

    public void deleteNotePermanently(String noteId) {
        repository.deleteNotePermanently(noteId);
    }
}
