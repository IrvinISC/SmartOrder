package com.example.irvin.smartorder;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimerTask;

public class iniciar_sesion extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private EditText usuario, pass;
    private Button aceptar;

    private RequestQueue rq;
    private JsonRequest jrq;

    private String user, password, nombre;
    private LottieAnimationView usuarios, contrasenas;
    private static final long DURATION_TRANSITION = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        getSupportActionBar().hide();

        usuario = (EditText) findViewById(R.id.ED_usuario);
        pass = (EditText) findViewById(R.id.ED_pass);
        aceptar = (Button) findViewById(R.id.BTN_aceptar);
        usuarios = (LottieAnimationView) findViewById(R.id.LA_usuario);
        contrasenas = (LottieAnimationView) findViewById(R.id.LA_pass);

        rq = Volley.newRequestQueue(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Fade fade = new Fade(Fade.IN);
            fade.setDuration(iniciar_sesion.DURATION_TRANSITION);
            fade.setInterpolator(new DecelerateInterpolator());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Slide slideBottom = new Slide(Gravity.START);
                slideBottom.setDuration(DURATION_TRANSITION);
                slideBottom.setInterpolator(new DecelerateInterpolator());
                getWindow().setReenterTransition(fade);
                getWindow().setReturnTransition(slideBottom);
                getWindow().setAllowReturnTransitionOverlap(false);
            }
        }
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
            if(resultado.equals("Usuario y/o contraseña incorrectos")){
                Toast.makeText(iniciar_sesion.this,"Usuario y/o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                usuarios.setAnimation("advertencia.json");
                usuarios.playAnimation();
                contrasenas.setAnimation("advertencia.json");
                contrasenas.playAnimation();
            }else{
                JSONObject datosJSON = new JSONObject(response.getString("resultado"));
                nombre = datosJSON.getString("Nombre");
                user = datosJSON.getString("Usuario");
                password = datosJSON.getString("contrasena");
                if(password.equals(pass.getText().toString())){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        /*transition.setDuration(DURATION_TRANSITION);
                        transition.setInterpolator(new DecelerateInterpolator());*/
                        TransitionSet transitionSet =  new TransitionSet();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Slide slide = new Slide(Gravity.START);
                            Fade fadeOut = new Fade(Fade.OUT);
                            transitionSet.addTransition(slide);
                            transitionSet.addTransition(fadeOut);
                            transitionSet.setDuration(DURATION_TRANSITION);
                            transitionSet.setInterpolator(new DecelerateInterpolator());
                            getWindow().setExitTransition(transitionSet);
                        }
                    }
                    limpiarCampos();
                    Intent intent = new Intent(iniciar_sesion.this,escoger_lugar.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());

                    }else{
                        startActivity(intent);
                    }
                    Toast.makeText(iniciar_sesion.this,"Bienvenido "+nombre,Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(iniciar_sesion.this,"Usuario y/o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void limpiarCampos(){
        usuario.setText("");
        pass.setText("");
    }

    public void onExplodeClicked(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            clic(view);
        }

    }
}
