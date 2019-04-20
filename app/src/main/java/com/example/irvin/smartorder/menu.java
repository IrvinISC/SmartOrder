package com.example.irvin.smartorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class menu extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private RequestQueue rq;
    private JsonRequest jrq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();

        rq = Volley.newRequestQueue(this);

        crearListaCategorias();
    }

    public void crearListaCategorias(){
        String url = "https://ezjr.000webhostapp.com/PHP/getCategorias.php";
        jrq = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this,error+"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            //Recuperar el JSONArray que devuelve la consulta con la clave resultado
            JSONArray arreglo = new JSONArray(response.getString("resultado"));
            //Separar cada elemento del arreglo en un JSONObject
            JSONObject[] datos = new JSONObject[arreglo.length()];
            for(int i = 0;i < arreglo.length();i++){
                datos[i] = (JSONObject) arreglo.get(i);
            }
            //Recuperar los nombres de cada categoria en un arreglo de String
            ArrayList<String> nombres = new ArrayList<>();
            for(int i = 0;i < arreglo.length();i++){
                nombres.add(datos[i].getString("nombre"));
            }
            Intent intent = new Intent(this, lista_categoria.class);
            intent.putExtra("nombre",nombres);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
