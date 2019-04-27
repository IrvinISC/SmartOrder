package com.example.irvin.smartorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class baseDatosLocal extends SQLiteOpenHelper {

    String Productos_seleccionados = "" +
            "CREATE TABLE pro_seleccionados (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT,precio TEXT,ingredientes TEXT)";

    public baseDatosLocal(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Productos_seleccionados);
        Log.d("crear","Creando base de datos");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS pro_seleccionados");
        db.execSQL(Productos_seleccionados);
        Log.d("actualizar","Actualizando base de datos");
    }
}
