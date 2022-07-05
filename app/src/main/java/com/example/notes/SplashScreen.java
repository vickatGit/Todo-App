package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.notes.Database.UserEntity;
import com.example.notes.HomeActivity;
import com.example.notes.LoginActivity;
import com.example.notes.R;
import com.example.notes.ViewModels.SignupLoginViewModel;

import java.util.List;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "tag";
    SignupLoginViewModel loginViewModel;
    ImageView splash_screen_image;
    TextView splash_screen_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splash_screen_image=findViewById(R.id.splash_screen_image);
        splash_screen_content=findViewById(R.id.splash_content);
        splash_screen_content.setTypeface(ResourcesCompat.getFont(this,R.font.montserratmedium));
        loginViewModel = new ViewModelProvider(SplashScreen.this).get(SignupLoginViewModel.class);

        splash_screen_image.startAnimation(AnimationUtils.loadAnimation(this,R.anim.splash_screen));
        Glide.with(this).load(R.drawable.splash_screen).into(splash_screen_image);
        splash_screen_content.startAnimation(AnimationUtils.loadAnimation(this,R.anim.splash_screen_content));
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                loginViewModel.getUser().observe(SplashScreen.this, new Observer<List<UserEntity>>() {
                    @Override
                    public void onChanged(List<UserEntity> userEntities) {
                        Log.d(TAG, "onChanged: "+userEntities.size());
                        if(userEntities.size()>0) {
                            Log.d(TAG, "onChanged: "+userEntities.get(0).getUser_name());
                            if (userEntities.get(0).getUser_name() != null) {
                                Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                                intent.putExtra("userId",userEntities.get(0).getUser_id());
                                intent.putExtra("userName",userEntities.get(0).getUser_name());
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
            }
        },2000);


        

    }
}