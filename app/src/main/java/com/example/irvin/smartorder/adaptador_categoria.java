package com.example.irvin.smartorder;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class adaptador_categoria extends ArrayAdapter<categoria> implements Response.Listener<JSONObject>, Response.ErrorListener {

    Context context;
    int resource;
    List<categoria> lista_cat;
    private RequestQueue rq;
    private JsonRequest jrq;

    List<producto> lista_pro;
    ListView listView, listView2;
    Button ordenar;
    ArrayList<String> ids;
    ArrayList<String> nombres;
    ArrayList<String> precios;
    ArrayList<String> ingredientes;

    Activity activity;
    public adaptador_categoria(Context context, int resource, List<categoria> lista_cat, ListView listView2, Button ordenar, Activity activity){
        super(context,resource,lista_cat);

        this.context = context;
        this.resource = resource;
        this.lista_cat = lista_cat;
        this.listView2 = listView2;
        this.ordenar = ordenar;
        this.activity = activity;

        lista_pro = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource,null);

        final Button nombre = (Button) view.findViewById(R.id.BTN_categoria);

        categoria categoria = lista_cat.get(position);

        nombre.setText(categoria.getNombre());

        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;
                String txt = btn.getText().toString();
                Toast.makeText(context,txt,Toast.LENGTH_SHORT).show();
                rq = Volley.newRequestQueue(context);
                crearListaProductos(txt);
            }
        });
        return  view;
    }

    public void crearListaProductos(String categoria){
        String url = "https://ezjr.000webhostapp.com/PHP/getProductos_categoria.php?categoria="+categoria;
        jrq = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(context,error+"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            //----------PRODUCTOS------------
            JSONArray arreglo = new JSONArray(response.getString("resultado"));
            JSONObject[] datos = new JSONObject[arreglo.length()];
            for(int i = 0;i < arreglo.length();i++){
                datos[i] = (JSONObject) arreglo.get(i);
            }
            ids = new ArrayList<>();
            nombres = new ArrayList<>();
            precios = new ArrayList<>();
            ingredientes = new ArrayList<>();
            for(int i = 0;i < arreglo.length();i++){
                ids.add(datos[i].getString("id"));
                nombres.add(datos[i].getString("nombre"));
                precios.add(datos[i].getString("precio"));
                ingredientes.add(datos[i].getString("ingredientes"));
            }

            lista_pro = new ArrayList<>();
            for(int i = 0;i < arreglo.length();i++){
                lista_pro.add(new producto(ids.get(i),nombres.get(i),precios.get(i),ingredientes.get(i)));
            }
            adaptador_producto adaptador_producto = new adaptador_producto(context,R.layout.boton_producto,lista_pro,ordenar,activity);

            listView2.setAdapter(adaptador_producto);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
