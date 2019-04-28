package com.example.irvin.smartorder;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class escoger_lugar extends AppCompatActivity {

    private static final long DURATION_TRANSITION = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_lugar);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Fade fade = new Fade(Fade.IN);
            fade.setDuration(DURATION_TRANSITION);
            fade.setInterpolator(new DecelerateInterpolator());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Slide slideTop = new Slide(Gravity.END);
                slideTop.setDuration(DURATION_TRANSITION);
                slideTop.setInterpolator(new DecelerateInterpolator());
                getWindow().setEnterTransition(fade);
                getWindow().setReturnTransition(slideTop);
                getWindow().setAllowEnterTransitionOverlap(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
    }

    public void menu(View view){
        Intent intent = new Intent(this, preparar_menu.class);
        startActivity(intent);
    }
}
