package com.example.irvin.smartorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        getSupportActionBar().hide();

    }

    public void registrarse(View view){
        Intent intent = new Intent(this,registrarse.class);
        startActivity(intent);
    }
    public void iniciarSesion(View view){
        Intent intent = new Intent(this,iniciar_sesion.class);
        startActivity(intent);
    }
    public void escoger(View view){
        Intent intent = new Intent(this,escoger_lugar.class);
        startActivity(intent);
    }
}
