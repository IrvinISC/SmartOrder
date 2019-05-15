package com.example.irvin.smartorder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ordenar extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    List<producto> lista_pro;
    ListView listView;

    ArrayList<String> ids_pro;
    ArrayList<String> nombres;
    ArrayList<String> precios;
    ArrayList<String> ingredientes;
    ArrayList<String> estados;
    private int VERSION = 9;
    View dialogo, confirmar;

    Button BTN_conf,BTN_cancelar;
    private RequestQueue rq;
    private JsonRequest jrq;

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
        estados = new ArrayList<>();
        dialogo = getLayoutInflater().inflate(R.layout.dialog_editar_ing,null);
        confirmar = getLayoutInflater().inflate(R.layout.dialogo_confirmar,null);
        BTN_conf = (Button) findViewById(R.id.BTN_confirmar_ord);
        BTN_cancelar = (Button) findViewById(R.id.BTN_cancelar_ord);

        reporteProducto();
        dialogo_confirmar(BTN_conf);
        dialogo_cancelar(BTN_cancelar);
        rq = Volley.newRequestQueue(this);
    }

    public void reporteProducto() {
        baseDatosLocal md = new baseDatosLocal(this,"BD_SO",null, VERSION);
        SQLiteDatabase bd = md.getReadableDatabase();
        Cursor c  = bd.rawQuery("SELECT * FROM pro_seleccionados WHERE activo = '1'",null);
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

        listView = (ListView) findViewById(R.id.LV_cuenta);
        adaptador_orden adaptador_orden = new adaptador_orden(this,R.layout.lista_orden,lista_pro,listView,dialogo);

        listView.setAdapter(adaptador_orden);
    }
    public void reporteIngredientes(){
        baseDatosLocal md = new baseDatosLocal(this,"BD_SO",null, VERSION);
        SQLiteDatabase bd = md.getReadableDatabase();
        Cursor c  = bd.rawQuery("SELECT * FROM pro_seleccionados WHERE activo = '1'",null);
        Cursor c2;
        String nombre = "", precio = "", ingrediente = "";
        if(c.moveToFirst()){
            do{
                nombre += c.getString(1) + ",";
                precio += c.getString(2) + ",";
                c2 = bd.rawQuery("SELECT * FROM ing_seleccionados WHERE id_pro = " + c.getInt(0),null);
                if(c2.moveToFirst()){
                    do{
                        if(c2.getString(2).equals("1")){
                            ingrediente += c2.getString(1) + ",";
                        }
                    }while (c2.moveToNext());
                }
                actualizarProductos(c.getInt(0));
                ingrediente += ";";
            }while (c.moveToNext());
        }else {
            Toast.makeText(this,"No hay productos registrados",Toast.LENGTH_SHORT).show();
        }
        bd.close();
        String usuario = reporteUsuarios();
        ordenar(nombre,precio,ingrediente,usuario,"1","2");
    }
    public String reporteUsuarios(){
        baseDatosLocal md = new baseDatosLocal(this,"BD_SO",null, VERSION);
        SQLiteDatabase bd = md.getReadableDatabase();
        Cursor c  = bd.rawQuery("SELECT * FROM usuario",null);
        String usuario = "", nombre = "", contrasena = "";
        if(c.moveToFirst()){
            do{
                usuario = c.getString(0);
                nombre = c.getString(1);
                contrasena = c.getString(2);
            }while (c.moveToNext());
        }else {
            usuario = "";
        }
        bd.close();
        return usuario;
    }
    public void actualizarProductos(int id) {
        baseDatosLocal md = new baseDatosLocal(this,"BD_SO",null, VERSION);
        SQLiteDatabase bd = md.getWritableDatabase();
        String consulta = "UPDATE pro_seleccionados SET activo = '0' WHERE id = " + id;
        bd.execSQL(consulta);
        bd.close();
    }
    public void bajasProducto() {
        baseDatosLocal md = new baseDatosLocal(this,"BD_SO",null,VERSION);
        SQLiteDatabase bd = md.getWritableDatabase();
        Cursor c  = bd.rawQuery("SELECT id FROM pro_seleccionados WHERE activo = '1'",null);
        ArrayList<Integer> ids_pro = new ArrayList<>();
        int id;
        if(c.moveToFirst()){
            do{
                id = c.getInt(0);
                ids_pro.add(id);
            }while (c.moveToNext());
        }else {}
        ArrayList<Integer> ids_ing = new ArrayList<>();
        Cursor c2;
        for(int i = 0;i < ids_pro.size();i++){
            c2 = bd.rawQuery("SELECT id FROM ing_seleccionados WHERE id_pro = " + ids_pro.get(i),null);
            if(c2.moveToFirst()){
                do{
                    ids_ing.add(c2.getInt(0));
                }while (c2.moveToNext());
            }
        }
        for(int i = 0;i < ids_ing.size();i++){
            String consulta2 = "DELETE FROM ing_seleccionados WHERE id = " + ids_ing.get(i);
            bd.execSQL(consulta2);
        }
        for(int i = 0;i < ids_pro.size();i++){
            String consulta = "DELETE FROM pro_seleccionados WHERE id = " + ids_pro.get(i);
            bd.execSQL(consulta);
        }
        bd.close();
    }
    public void dialogo_confirmar(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ordenar.this);
                mBuilder.setView(confirmar);
                final AlertDialog alertDialog = mBuilder.show();
                alertDialog.setCanceledOnTouchOutside(false);

                Window window = alertDialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView titulo = (TextView) confirmar.findViewById(R.id.TV_titulo_orden);
                Button cancelar = (Button) confirmar.findViewById(R.id.BTN_no);
                Button conf = (Button) confirmar.findViewById(R.id.BTN_si);
                titulo.setText("¿Pedir tu orden?");

                conf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reporteIngredientes();
                        ((ViewGroup)confirmar.getParent()).removeView(confirmar);
                        alertDialog.dismiss();
                        Intent intent = new Intent(ordenar.this,preparar_menu.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewGroup)confirmar.getParent()).removeView(confirmar);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            ((ViewGroup)confirmar.getParent()).removeView(confirmar);
                            alertDialog.dismiss();
                        }
                        return true;
                    }
                });
            }
        });
    }
    public void dialogo_cancelar(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ordenar.this);
                mBuilder.setView(confirmar);
                final AlertDialog alertDialog = mBuilder.show();
                alertDialog.setCanceledOnTouchOutside(false);

                Window window = alertDialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView titulo = (TextView) confirmar.findViewById(R.id.TV_titulo_orden);
                Button cancelar = (Button) confirmar.findViewById(R.id.BTN_no);
                Button conf = (Button) confirmar.findViewById(R.id.BTN_si);
                titulo.setText("¿Cancelar tu orden?");

                conf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bajasProducto();
                        Toast.makeText(ordenar.this,"Orden cancelada",Toast.LENGTH_SHORT).show();
                        ((ViewGroup)confirmar.getParent()).removeView(confirmar);
                        alertDialog.dismiss();
                        Intent intent = new Intent(ordenar.this,preparar_menu.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewGroup)confirmar.getParent()).removeView(confirmar);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            ((ViewGroup)confirmar.getParent()).removeView(confirmar);
                            alertDialog.dismiss();
                        }
                        return true;
                    }
                });
            }
        });
    }

    public void ordenar(String productos, String precios, String ingredientes, String usuario, String lugar, String mesa){
        String url = "https://ezjr.000webhostapp.com/PHP/insertar_orden.php?productos="+productos+
                "&precios="+precios+"&ingredientes="+ingredientes+"&usuario="+usuario+
                "&lugar="+lugar+"&no_mesa="+mesa;
        jrq = new JsonObjectRequest(Request.Method.POST,url,null,this,this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this,error+"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            String r = response.getString("resultado");
            Toast.makeText(this,r,Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
