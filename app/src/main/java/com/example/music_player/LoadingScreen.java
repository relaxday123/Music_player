package com.example.music_player;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingScreen extends AppCompatActivity {

    private boolean isMinimized = false;
    LinearLayout L1,L2;
    TextView tv1,tv2;
    Animation Fade;
    ImageView img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_loading_screen);
//        getSupportActionBar().hide();


        L1 = (LinearLayout)findViewById(R.id.l1);

        tv1 = (TextView)findViewById(R.id.text1);

        tv2 = (TextView)findViewById(R.id.text2);

        img1 = (ImageView)findViewById(R.id.waves);

        Fade = AnimationUtils.loadAnimation(this,R.anim.fade);

        img1.setAnimation(Fade);

        Intent i = new Intent(LoadingScreen.this,MainActivity.class);

        Thread thread = new Thread()
        {
            @Override
            public void run(){
                try {
                    sleep(0);
                    finish();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                }
            }
        };thread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isMinimized = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isMinimized) {
            startActivity(new Intent(this, LoadingScreen.class));
            isMinimized = false;
            finish();
        }
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}