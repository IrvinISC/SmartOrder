package com.example.irvin.smartorder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

public class adaptador_cuenta extends ArrayAdapter<producto> {

    Context context;
    int resource;
    List<producto> lista_pro;
    int cantidades [];

    public adaptador_cuenta(final Context context, int resource, List<producto> lista_pro, int cantidades[]) {
        super(context,resource,lista_pro);

        this.context = context;
        this.resource = resource;
        this.lista_pro = lista_pro;
        this.cantidades = cantidades;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource,null);

        TextView cantidad = (TextView) view.findViewById(R.id.TV_cantidad_cuenta);
        TextView producto = (TextView) view.findViewById(R.id.TV_producto_cuenta);
        TextView precio = (TextView) view.findViewById(R.id.TV_precio);
        TextView importe = (TextView) view.findViewById(R.id.TV_importe);

        final producto pro = lista_pro.get(position);

        cantidad.setText(String.valueOf(cantidades[position]));
        producto.setText(pro.getNombre());
        precio.setText(pro.getPrecio());
        importe.setText(String.valueOf(cantidades[position] * Integer.parseInt(pro.getPrecio())));

        return  view;
    }
}
