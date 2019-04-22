package com.example.irvin.smartorder;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class adaptador_categoria extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater = null;
    public Resources res;
    categoria categoria = null;
    int i = 0;

    public adaptador_categoria(Activity activity, ArrayList data, Resources res) {
        this.activity = activity;
        this.data = data;
        this.res = res;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter","=====Row button clicked=====");
    }

    @Override
    public int getCount() {
        if(data.size()<=0)
            return 1;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        public Button button;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if(convertView == null){

            view = inflater.inflate(R.layout.boton_categoria,null);

            holder = new ViewHolder();
            holder.button = (Button) view.findViewById(R.id.BTN_categoria);

            view.setTag(holder);
        }else
            holder = (ViewHolder) view.getTag();
        if(data.size() <= 0){
            holder.button.setText("No hay categorias");
        }else{
            categoria = null;
            categoria = (categoria) data.get(position);
            holder.button.setText(categoria.getNombre());
            view.setOnClickListener(new OnItemClickListener(position));
        }
        return view;
    }
    private class OnItemClickListener implements View.OnClickListener{

        private int mPosition;

        public OnItemClickListener(int mPosition) {
            this.mPosition = mPosition;
        }

        @Override
        public void onClick(View v) {
            menu lista_categoria = (menu)activity;
            lista_categoria.onItemClick(mPosition);
        }

    }
}
