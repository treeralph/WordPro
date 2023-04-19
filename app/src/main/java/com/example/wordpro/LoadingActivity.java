package com.example.wordpro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

public class LoadingActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        activityViewInitializer();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, SignInActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoadingActivity.this, imageView, "loadingActivityTransition");
                startActivity(intent, options.toBundle());
            }
        }, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void activityViewInitializer(){
        imageView = findViewById(R.id.loadingActivityImageView);
    }

}