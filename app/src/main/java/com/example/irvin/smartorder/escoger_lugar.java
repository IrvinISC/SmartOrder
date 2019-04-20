package com.example.irvin.smartorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class escoger_lugar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_lugar);
        getSupportActionBar().hide();
    }

    public void menu(View view){
        Intent intent = new Intent(this, menu.class);
        startActivity(intent);
    }
}
