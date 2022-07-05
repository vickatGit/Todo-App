package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import at.favre.lib.crypto.bcrypt.BCrypt;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.notes.Database.UserEntity;
import com.example.notes.ViewModels.LoginActivityViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "tag";


    EditText username;
    EditText fullName;
    EditText password;
    EditText retypePassword;
    TextInputLayout passwordContainer;
    TextInputLayout retypePasswordContainer;
    TextInputLayout usernamecontainer;
    Button createAccount;
    LoginActivityViewModel loginViewModel;
    ProgressBar progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();
        loginViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        loginViewModel.getUser().observe(this, new Observer<List<UserEntity>>() {
            @Override
            public void onChanged(List<UserEntity> userEntities) {
                Log.d(TAG, "signup onChanged: "+userEntities.size());
                if(userEntities.size()>0) {
                    Log.d(TAG, "onChanged: " + userEntities.get(0).getUser_name());
                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });



        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFullNameValid(fullName.getText().toString()) && isUsernameValid(username.getText().toString()) && isPasswordValid(password.getText().toString(),retypePassword.getText().toString())){
                    progress.setVisibility(View.VISIBLE);

                    retypePasswordContainer.setErrorEnabled(false);
                    usernamecontainer.setErrorEnabled(false);
                    passwordContainer.setErrorEnabled(false);

                    HashMap<String,Object> userData=new HashMap<>();
                    userData.put("user_name",username.getText().toString().trim());
                    userData.put("full_name",fullName.getText().toString().trim());

                    HandlerThread handlerThread=new HandlerThread("password");
                    handlerThread.start();
                    new Handler(handlerThread.getLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                                    String hashedPassword= BCrypt.withDefaults().hashToString(12,password.getText().toString().trim().toCharArray());
                                    Log.d(TAG, "onClick: "+hashedPassword);
//                                    String hashedUsername=BCrypt.withDefaults().hashToString(12,username.getText().toString().toCharArray());
                                    userData.put("password",hashedPassword);

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();


                            db.collection("USERS")
                                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if(!queryDocumentSnapshots.isEmpty()) {
                                        Log.d(TAG, "onSuccess: ");
                                        db.collection("USERS")
                                                .whereEqualTo("user_name",username.getText().toString()).get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                        if(queryDocumentSnapshots.size()==0){

                                            String userId=db.collection("USERS").document().getId();
                                            db.collection("USERS").document(userId).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "onSuccess: "+"Account Created successFully");
                                                }
                                            });
                                            UserEntity userEntity = new UserEntity(username.getText().toString(), username.getText().toString());
                                            loginViewModel.addUser(userEntity);
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progress.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(MainActivity.this,"Account Created SuccesFully",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else {
                                            Log.d(TAG, "onSuccess: "+queryDocumentSnapshots.size()+queryDocumentSnapshots.getDocuments().get(0).getString("user_name"));
                                            usernamecontainer.setError("Username is Already Taken");
                                            Toast.makeText(MainActivity.this,"You should Try using Login",Toast.LENGTH_SHORT).show();
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progress.setVisibility(View.INVISIBLE);
                                                }
                                            });
                                        }
                                    }
                                    });
                                    }

                                    else {
                                        Log.d(TAG, "onSuccess: " + "empty");
                                        String userId=db.collection("USERS").document().getId();
                                        Log.d(TAG, "onSuccess: "+userId);
                                        db.collection("USERS").document(userId).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "onSuccess: "+"Account Created successFully");
                                            }
                                        });
                                        UserEntity userEntity = new UserEntity(username.getText().toString(), username.getText().toString());
                                        loginViewModel.addUser(userEntity);
                                    }
//

                                }
                            });

                        }
                    });
                }
                else
                    Toast.makeText(MainActivity.this,"Please Enter Correct Credentials",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initialise(){
        username=findViewById(R.id.user_name_edit);
        fullName=findViewById(R.id.user_full_name_edit);
        password=findViewById(R.id.password_edit);
        retypePassword=findViewById(R.id.retype_password_edit);
        createAccount=findViewById(R.id.create_account);
        progress=findViewById(R.id.progress);
        passwordContainer=findViewById(R.id.password);
        retypePasswordContainer=findViewById(R.id.retype_password);
        usernamecontainer=findViewById(R.id.user_name);
    }
    public boolean isUsernameValid(String username){
        if(username!=null){
            return true;
        }
        else
            return false;
    }
    public boolean isFullNameValid(String fullName){
        if(fullName!=null){
            return true;
        }
        else
            return false;
    }
    public boolean isPasswordValid(String password,String retypePassword){
        if(password ==null){
            return false;
        }
        else if(password.length()<5){
            this.passwordContainer.setError("Password Length Should be Greater than 5");
            return false;
        }
        else if(password.equals(retypePassword)){
            return true;
        }
        else {
            this.retypePasswordContainer.setError("Password mismatched");
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(loginViewModel!=null){
            this.fullName.setText(loginViewModel.fullName);
            this.username.setText(loginViewModel.username);
            this.password.setText(loginViewModel.password);
            this.retypePassword.setText(loginViewModel.retypePassword);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        loginViewModel.fullName=this.fullName.getText().toString();
        loginViewModel.username=this.username.getText().toString();
        loginViewModel.password=this.password.getText().toString();
        loginViewModel.retypePassword=this.retypePassword.getText().toString();
    }
}