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

public class iniciar_sesion extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private EditText usuario, pass;
    private Button aceptar;

    private RequestQueue rq;
    private JsonRequest jrq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        getSupportActionBar().hide();

        usuario = (EditText) findViewById(R.id.ED_usuario);
        pass = (EditText) findViewById(R.id.ED_pass);
        aceptar = (Button) findViewById(R.id.BTN_aceptar);

        rq = Volley.newRequestQueue(this);
    }

    public void clic(View view){
        if(!usuario.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()){
            iniciarSesion();
        }else
            Toast.makeText(this,"Llena todos los campos",Toast.LENGTH_SHORT).show();
    }

    public void iniciarSesion(){
        String url = "https://ezjr.000webhostapp.com/PHP/getUsuario_nomUsuario.php?usuario="+usuario.getText().toString();
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
            String resultado = response.getString("resultado");
            Toast.makeText(iniciar_sesion.this,resultado,Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void limpiarCampos(){
        usuario.setText("");
        pass.setText("");
    }
}
