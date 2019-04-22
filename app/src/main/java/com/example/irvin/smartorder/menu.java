package com.example.irvin.smartorder;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class menu extends AppCompatActivity {

    ListView lista;
    adaptador_categoria adaptador_categoria;
    public menu menu = null;
    public ArrayList<categoria> arrayList = new ArrayList<categoria>();
    ArrayList<String> nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();

        menu = this;
        nom = getIntent().getStringArrayListExtra("nombre");
        for(int i = 0;i < nom.size();i++){
            setListData(nom.get(i));
        }
        Resources resources = getResources();
        lista = (ListView) findViewById(R.id.LV_categoria);

        adaptador_categoria = new adaptador_categoria(menu,arrayList,resources);
        lista.setAdapter(adaptador_categoria);
    }

    public void setListData(String nombre){
        final categoria categoria = new categoria();
        categoria.setNombre(nombre);
        arrayList.add(categoria);
    }

    public void onItemClick(int mPosition){
        categoria categoria = (com.example.irvin.smartorder.categoria) arrayList.get(mPosition);
        Toast.makeText(menu.this,categoria.getNombre(),Toast.LENGTH_SHORT).show();

    }
}
