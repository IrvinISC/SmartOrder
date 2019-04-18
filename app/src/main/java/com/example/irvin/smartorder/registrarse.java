package com.example.irvin.smartorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class registrarse extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private EditText nombre, usuario, pass1, pass2;
    private Button aceptar;

    private RequestQueue rq;
    private JsonRequest jrq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        getSupportActionBar().hide();

        nombre = (EditText) findViewById(R.id.ED_nombre);
        usuario = (EditText) findViewById(R.id.ED_usuario);
        pass1 = (EditText) findViewById(R.id.ED_pass1);
        pass2 = (EditText) findViewById(R.id.ED_pass2);
        aceptar = (Button) findViewById(R.id.BTN_aceptar);

        rq = Volley.newRequestQueue(this);
    }

    public void clic(View view){
        if(!nombre.getText().toString().isEmpty() && !usuario.getText().toString().isEmpty() &&
        !pass1.getText().toString().isEmpty() && !pass2.getText().toString().isEmpty()){
            if(pass1.getText().toString().equals(pass2.getText().toString())){
                registrar();
            }
        }else
            Toast.makeText(this,"Llena todos los campos",Toast.LENGTH_SHORT).show();
    }

    public void registrar(){
        String url = "https://ezjr.000webhostapp.com/PHP/insertar_usuario.php?nombre="+nombre.getText().toString()
                +"&usuario="+usuario.getText().toString()+"&contrasena="+pass1.getText().toString();
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
            String resultado = response.getString("resultado");
            Toast.makeText(registrarse.this,resultado,Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
