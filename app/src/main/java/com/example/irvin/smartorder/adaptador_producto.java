package com.example.irvin.smartorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class adaptador_producto extends ArrayAdapter<producto> {

    Context context;
    int resource;
    List<producto> lista_pro;
    Button ordenar;
    private int VERSION = 5;
    Activity activity;
    boolean bandera = true;

    public adaptador_producto(final Context context, int resource, List<producto> lista_pro, Button ordenar, final Activity activity) {
        super(context,resource,lista_pro);

        this.context = context;
        this.resource = resource;
        this.lista_pro = lista_pro;
        this.ordenar = ordenar;
        this.activity = activity;

        ordenar.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                if(bandera || reporteProducto().equals("No hay productos registrados")){
                    Toast.makeText(context,"No has seleccionado tu comida",Toast.LENGTH_SHORT).show();
                }
                if(!bandera || reporteProducto().equals("")){
                    Intent intent = new Intent(context,ordenar.class);
                    context.startActivity(intent);
                }
                }
            });
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource,null);

        Button button = (Button) view.findViewById(R.id.BTN_des);

        final producto producto = lista_pro.get(position);

        button.setText(producto.getNombre()+", Precio: "+producto.getPrecio());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bandera = false;
                agregarProducto(lista_pro.get(position).getNombre(),lista_pro.get(position).getPrecio(),lista_pro.get(position).getIngredientes());
            }
        });
        return  view;
    }
    public void agregarProducto(String nombre, String precio, String ingredientes) {
        baseDatosLocal md = new baseDatosLocal(context,"BD_SO",null, VERSION);
        SQLiteDatabase bd = md.getWritableDatabase();
        String consulta = "INSERT INTO pro_seleccionados(nombre,precio,ingredientes) VALUES('"+nombre+"','"+precio+"','"+ingredientes+"')";
        bd.execSQL(consulta);
        bd.close();
        Toast.makeText(context,"Producto agregado",Toast.LENGTH_SHORT).show();
    }
    public String reporteProducto() {
        baseDatosLocal md = new baseDatosLocal(context, "BD_SO", null, VERSION);
        SQLiteDatabase bd = md.getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT * FROM pro_seleccionados", null);
        if (c.moveToFirst()) {
            do {
            } while (c.moveToNext());

        } else {
            return "No hay productos registrados";
        }
        bd.close();
        return "";
    }
}
