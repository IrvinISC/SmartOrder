package com.example.irvin.smartorder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class menu extends AppCompatActivity {

    List<categoria> lista_cat;
    ListView listView,listView2;
    Button ordenar;
    ArrayList<String> nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();

        nom = getIntent().getStringArrayListExtra("nombre");
        lista_cat = new ArrayList<>();
        for(int i = 0;i < nom.size();i++){
            lista_cat.add(new categoria(nom.get(i)));
        }
        ordenar = (Button) findViewById(R.id.BTN_ordenar);
        listView = (ListView) findViewById(R.id.LV_categoria);
        listView2 = (ListView) findViewById(R.id.LV_productos);
        adaptador_categoria adaptador_categoria = new adaptador_categoria(this,R.layout.boton_categoria,lista_cat,listView2,ordenar,this);

        listView.setAdapter(adaptador_categoria);

        ordenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(menu.this,"No has seleccionado tu comida",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
