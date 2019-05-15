package com.example.irvin.smartorder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class cuenta extends AppCompatActivity {

    List<producto> lista_pro;
    ListView lista;
    TextView subtotal,ivap,iva,total;
    private int VERSION = 9;
    int cantidades[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);
        getSupportActionBar().hide();

        lista_pro = new ArrayList<>();
        lista = (ListView) findViewById(R.id.LV_cuenta);
        subtotal = (TextView) findViewById(R.id.TV_subtotal2);
        ivap = (TextView) findViewById(R.id.TV_ivaP2);
        iva = (TextView) findViewById(R.id.TV_iva2);
        total = (TextView) findViewById(R.id.TV_Total2);
        reporteProducto();

        adaptador_cuenta adaptador_cuenta = new adaptador_cuenta(this,R.layout.lista_cuenta,lista_pro,cantidades);
        lista.setAdapter(adaptador_cuenta);
    }
    public void reporteProducto() {
        baseDatosLocal md = new baseDatosLocal(this, "BD_SO", null, VERSION);
        SQLiteDatabase bd = md.getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT * FROM pro_seleccionados WHERE activo = '0'", null);
        ArrayList<String> nombres = new ArrayList<>();
        ArrayList<String> todos_nombres = new ArrayList<>();
        String nom = "";
        int cont = 0;
        boolean bandera;
        if (c.moveToFirst()) {
            do {
                bandera = false;
                nom = c.getString(1);
                todos_nombres.add(nom);
                if(cont == 0){
                    nombres.add(nom);
                    cont++;
                    lista_pro.add(new producto(String.valueOf(c.getInt(0)),c.getString(1),c.getString(2)));
                }
                for(int i = 0;i < lista_pro.size();i++){
                    if(nom.equals(lista_pro.get(i).getNombre()) && cont > 0){
                        bandera = true;
                    }
                }
                if(bandera == false){
                    nombres.add(nom);
                    lista_pro.add(new producto(String.valueOf(c.getInt(0)),c.getString(1),c.getString(2)));
                }
            } while (c.moveToNext());

        } else {
        }
        bd.close();
        cantidades = new int[nombres.size()];

        for(int i = 0;i < nombres.size();i++){
            cantidades[i] = 0;
            for(int j = 0;j < todos_nombres.size();j++){
                if(nombres.get(i).equals(todos_nombres.get(j))){
                    cantidades[i]++;
                }
            }
        }
        float sub = 0;
        for(int i = 0;i < lista_pro.size();i++){
            sub += cantidades[i] * Float.parseFloat(lista_pro.get(i).getPrecio());
        }
        float i = (float) (sub * .16);
        String ivas = String.format("%.2f",i);
        subtotal.setText(String.valueOf(sub));
        ivap.setText("16");
        iva.setText(ivas);
        total.setText(String.valueOf(Float.parseFloat(ivas) + sub));
    }
}
