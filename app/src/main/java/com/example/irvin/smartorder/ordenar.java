package com.example.irvin.smartorder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class ordenar extends AppCompatActivity {

    List<producto> lista_pro;
    ListView listView;

    ArrayList<String> ids_pro;
    ArrayList<String> nombres;
    ArrayList<String> precios;
    ArrayList<String> ingredientes;
    private int VERSION = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenar);
        getSupportActionBar().hide();

        lista_pro = new ArrayList<>();
        ids_pro = new ArrayList<>();
        nombres = new ArrayList<>();
        precios = new ArrayList<>();
        ingredientes = new ArrayList<>();

        reporteProducto();
    }

    public void reporteProducto() {
        baseDatosLocal md = new baseDatosLocal(this,"BD_SO",null, VERSION);
        SQLiteDatabase bd = md.getReadableDatabase();
        Cursor c  = bd.rawQuery("SELECT * FROM pro_seleccionados",null);
        int id;
        String nombre = "", precio = "", ingrediente = "";
        if(c.moveToFirst()){
            do{
                id =  c.getInt(0);
                nombre = c.getString(1);
                precio = c.getString(2);
                ingrediente = c.getString(3);
                ids_pro.add(String.valueOf(id));
                nombres.add(nombre);
                precios.add(precio);
                ingredientes.add(ingrediente);
            }while (c.moveToNext());

        }else {
            Toast.makeText(this,"No hay productos registrados",Toast.LENGTH_SHORT).show();
        }
        bd.close();
        for(int i = 0;i < ids_pro.size();i++){
            lista_pro.add(new producto(ids_pro.get(i),nombres.get(i),precios.get(i),ingredientes.get(i)));
        }

        listView = (ListView) findViewById(R.id.LV_orden);
        adaptador_orden adaptador_orden = new adaptador_orden(this,R.layout.lista_orden,lista_pro,listView);

        listView.setAdapter(adaptador_orden);
    }
}
