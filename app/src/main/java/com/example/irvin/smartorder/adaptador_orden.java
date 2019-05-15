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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    List<ingrediente> lista_ing;
    ListView listView;
    ArrayList<String> ids_pro;
    ArrayList<String> nombres;
    ArrayList<String> precios;
    ArrayList<String> ingredientes;
    ArrayList<String> ing_separados;
    private int VERSION = 9;
    View dialogo;

    public adaptador_orden(Context context, int resource, List<producto> lista_pro, ListView listView, View dialogo){
        super(context,resource,lista_pro);

        this.context = context;
        this.resource = resource;
        this.lista_pro = lista_pro;
        this.listView = listView;
        this.dialogo = dialogo;
        ing_separados = new ArrayList<>();
        lista_ing = new ArrayList<>();

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

        String list_ing = lista_pro.get(position).getIngredientes();
        ing_separados.clear();
        lista_ing.clear();
        ingredientes(Integer.parseInt(lista_pro.get(position).getId()),list_ing);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar.playAnimation();

                String list_ing = lista_pro.get(position).getIngredientes();
                ing_separados.clear();
                lista_ing.clear();
                ingredientes(Integer.parseInt(lista_pro.get(position).getId()),list_ing);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                mBuilder.setView(dialogo);
                final AlertDialog alertDialog = mBuilder.show();
                alertDialog.setCanceledOnTouchOutside(false);

                Window window = alertDialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView nombre_ing = (TextView) dialogo.findViewById(R.id.TV_nombre_ing);
                final ListView ingredientes = (ListView) dialogo.findViewById(R.id.LV_ingredientes);
                final Button cancelar = (Button) dialogo.findViewById(R.id.BTN_cancelar_ing);
                Button aceptar = (Button) dialogo.findViewById(R.id.BTN_aceptar_ing);

                final adaptador_ingredientes adaptador_ingredientes = new adaptador_ingredientes(context,R.layout.lista_ingredientes,
                        lista_ing,aceptar,cancelar,alertDialog,ingredientes,dialogo);
                ingredientes.setAdapter(adaptador_ingredientes);
                nombre_ing.setText(lista_pro.get(position).getNombre());
            }
        });
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrar.playAnimation();
                bajasProducto(Integer.parseInt(lista_pro.get(position).getId()));
                reporteProducto();
                lista_pro = new ArrayList<>();
                for(int i = 0;i < ids_pro.size();i++){
                    lista_pro.add(new producto(ids_pro.get(i),nombres.get(i),precios.get(i),ingredientes.get(i)));
                }
                clear();
                adaptador_orden adaptador_orden = new adaptador_orden(context,R.layout.lista_orden,lista_pro,listView,dialogo);
                listView.setAdapter(adaptador_orden);
            }
        });
        return  view;
    }
    public void bajasProducto(int id) {
        baseDatosLocal md = new baseDatosLocal(context,"BD_SO",null,VERSION);
        SQLiteDatabase bd = md.getWritableDatabase();
        Cursor c  = bd.rawQuery("SELECT id FROM ing_seleccionados WHERE id_pro="+id,null);
        ArrayList<Integer> ids = new ArrayList<>();
        int id_ing;
        if(c.moveToFirst()){
            do{
                id_ing = c.getInt(0);
                ids.add(id_ing);
            }while (c.moveToNext());
        }else {}
        for(int i = 0;i < ids.size();i++){
            String consulta2 = "DELETE FROM ing_seleccionados WHERE id="+ids.get(i);
            bd.execSQL(consulta2);
        }
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
        Cursor c  = bd.rawQuery("SELECT * FROM pro_seleccionados WHERE  activo = '1'",null);
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
    public void ingredientes(int id_pro,String texto){
        String ing = texto, palabra = "";
        for(int i = 0;i<ing.length();i++){
            if(String.valueOf(ing.charAt(i)).equals(",")){
                ing_separados.add(palabra);
                palabra = "";
            }else{
                palabra += ing.charAt(i);
            }
        }
        reporteIngredientes(id_pro);

        if(lista_ing.isEmpty()){
            for(int i = 0;i < ing_separados.size();i++){
                agregarIngredientes(ing_separados.get(i),"1",id_pro);
            }
            reporteIngredientes(id_pro);
        }

    }
    public void agregarIngredientes(String ingrediente, String estado,int id_pro) {
        baseDatosLocal md = new baseDatosLocal(context,"BD_SO",null, VERSION);
        SQLiteDatabase bd = md.getWritableDatabase();
        String consulta = "INSERT INTO ing_seleccionados(ingrediente,estado,id_pro) VALUES('"+ingrediente+"','"+estado+"',"+id_pro+")";
        bd.execSQL(consulta);
        bd.close();
        //Toast.makeText(context,"Producto agregado",Toast.LENGTH_SHORT).show();
    }
    public void reporteIngredientes(int id_pro) {
        baseDatosLocal md = new baseDatosLocal(context, "BD_SO", null, VERSION);
        SQLiteDatabase bd = md.getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT * FROM ing_seleccionados WHERE id_pro="+id_pro, null);
        ArrayList<Integer> id = new ArrayList<>();
        ArrayList<String> nombre = new ArrayList<>();
        ArrayList<String> estado = new ArrayList<>();
        ArrayList<Integer> ids_pro = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                id.add(c.getInt(0));
                nombre.add(c.getString(1));
                estado.add(c.getString(2));
                ids_pro.add(c.getInt(3));
            } while (c.moveToNext());
            boolean estados[] = new boolean[estado.size()];
            for(int i = 0;i < estado.size();i++){
                if(estado.get(i).equals("1")){
                    estados[i] = true;
                }else{
                    estados[i] = false;
                }
            }
            for(int i = 0;i <id.size();i++){
                lista_ing.add(new ingrediente(id.get(i),nombre.get(i),estados[i],ids_pro.get(i)));
            }
        } else {
        }
        bd.close();
    }
}
