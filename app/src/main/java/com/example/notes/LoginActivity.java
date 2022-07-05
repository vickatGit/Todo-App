package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.notes.Database.UserEntity;
import com.example.notes.ViewModels.SignupLoginViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "tag";
    Button login;
    Button signup;
    EditText username;
    EditText password;
    SignupLoginViewModel loginViewModel;
    ProgressBar loginProgress;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        Log.d(TAG, "onCreate: ");
//        loginViewModel = new ViewModelProvider(this).get(SignupLoginViewModel.class);
//        loginViewModel.getUser().observe(this, new Observer<List<UserEntity>>() {
//            @Override
//            public void onChanged(List<UserEntity> userEntities) {
//                Log.d(TAG, "onChanged: "+userEntities.size());
//                if(userEntities.size()>0) {
//                    if (userEntities.get(0).getUser_name() != null) {
//                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                        intent.putExtra("userId",userEntities.get(0).getUser_id());
//                        intent.putExtra("userName",userEntities.get(0).getUser_name());
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            }
//        });
        initialise();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                FirebaseFirestore db=FirebaseFirestore.getInstance();
                db.collection("USERS")
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: ");
                            db.collection("USERS")
                                    .whereEqualTo("user_name", username.getText().toString()).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            Log.d(TAG, "onSuccess: queryDocumentSnapshots"+queryDocumentSnapshots.isEmpty());
                                            Log.d(TAG, "onSuccess: queryDocumentSnapshots Size"+queryDocumentSnapshots.size());
                                            if(queryDocumentSnapshots.size()>0){
                                                String encryptedPassword=queryDocumentSnapshots.getDocuments().get(0).getString("password");
                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        BCrypt.Result isCorrect= BCrypt.verifyer().verify(password.getText().toString().toCharArray(),encryptedPassword);
                                                        if(isCorrect.verified){
                                                            UserEntity userEntity=new UserEntity(queryDocumentSnapshots.getDocuments().get(0).getString("user_name"),queryDocumentSnapshots.getDocuments().get(0).getId());
                                                            loginViewModel.addUser(userEntity);
//                                                            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
//                                                            startActivity(intent);
                                                            loginProgress.setVisibility(View.INVISIBLE);
                                                            Log.d(TAG, "run: "+queryDocumentSnapshots.getDocuments().get(0).getId());
                                                        }
                                                        else {
                                                            loginProgress.setVisibility(View.INVISIBLE);
                                                            Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    }
                });

            }
        });





    }

    private void initialise() {
        login=findViewById(R.id.restore_note);
        signup=findViewById(R.id.sign_up);
        username=findViewById(R.id.user_name_edit);
        password=findViewById(R.id.password_edit);
        loginProgress=findViewById(R.id.login_progress);
    }

}