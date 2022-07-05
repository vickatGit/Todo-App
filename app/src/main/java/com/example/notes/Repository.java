package com.example.notes;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.notes.Database.UserDatabase;
import com.example.notes.Database.UserEntity;
import com.example.notes.Model.NoteModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class Repository {


    private static final String TAG = "tag";
    UserDatabase userDatabase;
    Context context;
    LiveData<List<UserEntity>> userEntity;
    MutableLiveData<List<NoteModel>> allNotes=new MutableLiveData<>();
    MutableLiveData<List<NoteModel>> allTrashNotes=new MutableLiveData<>();
    List<NoteModel> notes=new ArrayList<>();
    List<NoteModel> trashNotes=new ArrayList<>();
    boolean istrue=false;
    private Repository(Context context) {
        this.context=context;
        userDatabase=UserDatabase.getInstance(context);
        Log.d(TAG, "Repository: ");
        userEntity=userDatabase.getUserDao().getUserData();

    }

    public static Repository getInstance(Context context) {
        return new Repository(context);
    }
    public void addUser(UserEntity UserEntity){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                userDatabase.getUserDao().insertUser(UserEntity);
            }
        });


    }
    public void deleteUser(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                userDatabase.getUserDao().deleteAll();
            }
        });
    }
    public LiveData<List<UserEntity>> getUser(){
        return userEntity;
    }

    public void deleteAll() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                int effects=userDatabase.getUserDao().deleteAll();
                Log.d(TAG, "run: "+effects);
            }
        });
    }

    public void addNote(NoteModel noteModel) {
                FirebaseFirestore db=FirebaseFirestore.getInstance();

                if(noteModel.getNoteId()!=null){
                    db.collection("NOTES").document(noteModel.getNoteId()).update( "content",noteModel.getContent(),"timeStamp",noteModel.getDate()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Log.d(TAG, "onSuccess: updated Successfully "+noteModel.getNoteId());
                        }
                    });
                }
                else {
                    Log.d(TAG, "addNote: in else part");
                    HashMap<String,Object> noteMap=new HashMap<>();
                    noteMap.put("title",noteModel.getTitle());
                    noteMap.put("content",noteModel.getContent());
                    noteMap.put("timeStamp",noteModel.getDate());
                    noteMap.put("is_trash",noteModel.isTrash());
                    noteMap.put("user_reference",db.document("USERS/"+noteModel.getReference()));
                    String noteId = db.collection("NOTES").document().getId();

                    db.collection("NOTES").document(noteId).set(noteMap);
                }

    }
    public LiveData<List<NoteModel>> getTrashedNotes(){
        return allTrashNotes;
    }

    public LiveData<List<NoteModel>> getNotes(String userId) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
//        db.document("USERS/ "+userId);
        Log.d(TAG, "getNotes: userId"+userId);
        Log.d(TAG, "onSuccess: getNotes "+Thread.currentThread().getName());
        db.collection("NOTES").whereEqualTo("user_reference", db.document("USERS/"+userId)).orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "onSuccess: "+Thread.currentThread().getName());
                if(!queryDocumentSnapshots.isEmpty()){
                    notes.clear();
                    trashNotes.clear();
                    for (DocumentSnapshot document :queryDocumentSnapshots.getDocuments()){
//                        Date date=document.getTimestamp("timeStamp").toDate();



                        NoteModel noteModel=new NoteModel(document.getString("title")
                                ,document.getString("content")
                                ,document.getBoolean("is_trash")
                                ,document.getTimestamp("timeStamp").toDate());
                        noteModel.setNoteId(document.getId());
                        if(!noteModel.isTrash())
                            notes.add(noteModel);
                        else
                            trashNotes.add(noteModel);
                    }
                    allTrashNotes.postValue(trashNotes);
                    allNotes.postValue(notes);
                    Log.d(TAG, "onSuccess: "+queryDocumentSnapshots.getDocuments().size());
                    Log.d(TAG, "onSuccess: for loop is completed");
                    
                }
            }
        });
        Log.d(TAG, "getNotes: at Return");
        return allNotes;
    }

    public void deleteNote(String noteId) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("NOTES").document(noteId).update("is_trash",true);
    }

    public void restoreNote(String noteId) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("NOTES").document(noteId).update("is_trash",false);
    }

    public void deleteNotePermanently(String noteId) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("NOTES").document(noteId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: successfully deleted");
            }
        });

    }
}
