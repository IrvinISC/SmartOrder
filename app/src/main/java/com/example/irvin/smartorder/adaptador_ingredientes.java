package com.example.irvin.smartorder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class adaptador_ingredientes extends ArrayAdapter<ingrediente> {

    Context context;
    int resource;
    List<ingrediente> lista_ing;
    View dialogo;
    AlertDialog alertDialog;
    ListView ingredientes;
    private int VERSION = 9;
    boolean checked[];

    public adaptador_ingredientes(Context context, int resource, final List<ingrediente> lista_ing,
                                  Button aceptar, Button cancelar, AlertDialog alertDialog, ListView ingredientes, View dialogo) {
        super(context, resource, lista_ing);

        this.context = context;
        this.resource = resource;
        this.lista_ing = lista_ing;
        this.alertDialog = alertDialog;
        this.dialogo = dialogo;
        this.ingredientes = ingredientes;

        checked = new boolean[lista_ing.size()];
        for(int i = 0;i < lista_ing.size();i++){
            checked[i] = lista_ing.get(i).getEstado();
        }

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ids[] = new int[lista_ing.size()];
                for(int i = 0;i < lista_ing.size();i++){
                 ids[i] = lista_ing.get(i).getId();
                }
                actualizarIngrediente(ids,checked);
                limpiar();
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar();
            }
        });
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    limpiar();
                }
                return true;
            }
        });
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource,null);

        CheckBox ingrediente = (CheckBox) view.findViewById(R.id.CB_ingrediente);

        final ingrediente ingrediente1 = lista_ing.get(position);

        ingrediente.setText(ingrediente1.getNombre());

        ingrediente.setChecked(checked[position]);

        ingrediente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checked[position] = true;
                }else {
                    checked[position] = false;
                }
            }
        });
        ingrediente.setChecked(checked[position]);

        return  view;
    }

    public void limpiar(){
        lista_ing.clear();
        clear();
        ingredientes.setAdapter(null);
        ((ViewGroup)dialogo.getParent()).removeView(dialogo);
        alertDialog.dismiss();
    }
    public void actualizarIngrediente(int id[], boolean estado[]) {
        baseDatosLocal md = new baseDatosLocal(context,"BD_SO",null, VERSION);
        SQLiteDatabase bd = md.getWritableDatabase();
        for(int i = 0;i < lista_ing.size();i++){
            String es = "";
            if(estado[i]){
                es = "1";
            }else{
                es = "0";
            }
            String consulta = "UPDATE ing_seleccionados SET estado='"+es+"' WHERE id="+id[i];
            bd.execSQL(consulta);
        }
        bd.close();
    }
}
