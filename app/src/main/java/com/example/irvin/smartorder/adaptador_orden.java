package com.example.irvin.smartorder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

public class adaptador_orden extends ArrayAdapter<producto> {

    Context context;
    int resource;
    List<producto> lista_pro;
    ListView listView;
    ArrayList<String> ids_pro;
    ArrayList<String> nombres;
    ArrayList<String> precios;
    ArrayList<String> ingredientes;
    ArrayList<String> ing_separados;
    private int VERSION = 5;

    public adaptador_orden(Context context, int resource, List<producto> lista_pro, ListView listView){
        super(context,resource,lista_pro);

        this.context = context;
        this.resource = resource;
        this.lista_pro = lista_pro;
        this.listView = listView;
        ing_separados = new ArrayList<>();
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource,null);

        TextView pro = (TextView) view.findViewById(R.id.TV_producto);
        TextView precio = (TextView) view.findViewById(R.id.TV_precio);
        final LottieAnimationView editar = (LottieAnimationView) view.findViewById(R.id.LA_editar);
        final LottieAnimationView borrar = (LottieAnimationView) view.findViewById(R.id.LA_borrar);

        final producto producto = lista_pro.get(position);

        pro.setText(producto.getNombre());
        precio.setText("$"+producto.getPrecio());

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar.playAnimation();
                String lista_ing = lista_pro.get(position).getIngredientes();
                ingredientes(lista_ing);
                //Toast.makeText(context,lista_ing,Toast.LENGTH_SHORT).show();
            }
        });
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrar.playAnimation();
                /*Toast.makeText(context,"id: "+lista_pro.get(position).getId()+
                        ", nombre: "+lista_pro.get(position).getNombre()+
                        ", precio: "+lista_pro.get(position).getPrecio()+
                        ", ingredientes: "+lista_pro.get(position).getIngredientes(),Toast.LENGTH_SHORT).show();*/
                bajasProducto(Integer.parseInt(lista_pro.get(position).getId()));
                reporteProducto();
                lista_pro = new ArrayList<>();
                for(int i = 0;i < ids_pro.size();i++){
                    lista_pro.add(new producto(ids_pro.get(i),nombres.get(i),precios.get(i),ingredientes.get(i)));
                }
                clear();
                adaptador_orden adaptador_orden = new adaptador_orden(context,R.layout.lista_orden,lista_pro,listView);
                listView.setAdapter(adaptador_orden);
            }
        });
        return  view;
    }
    public void bajasProducto(int id) {
        baseDatosLocal md = new baseDatosLocal(context,"BD_SO",null,VERSION);
        SQLiteDatabase bd = md.getWritableDatabase();
        String consulta = "DELETE FROM pro_seleccionados WHERE id="+id;
        bd.execSQL(consulta);
        bd.close();
        Toast.makeText(context,"Producto eliminado",Toast.LENGTH_SHORT).show();
    }
    public void reporteProducto() {
        ids_pro = new ArrayList<>();
        nombres = new ArrayList<>();
        precios = new ArrayList<>();
        ingredientes = new ArrayList<>();
        baseDatosLocal md = new baseDatosLocal(context,"BD_SO",null, VERSION);
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
            Toast.makeText(context,"No hay productos registrados",Toast.LENGTH_SHORT).show();
        }
        bd.close();
    }
    public void ingredientes(String texto){
        String ing = texto, palabra = "";
        for(int i = 0;i<ing.length();i++){
            if(String.valueOf(ing.charAt(i)).equals(",")){
                ing_separados.add(palabra);
                palabra = "";
            }else{
                palabra += ing.charAt(i);
            }
        }
        for(int i = 0;i < ing_separados.size();i++){
            Toast.makeText(context,ing_separados.get(i),Toast.LENGTH_SHORT).show();
        }
    }
}
