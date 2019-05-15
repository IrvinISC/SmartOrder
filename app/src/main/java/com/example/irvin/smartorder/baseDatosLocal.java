package com.example.irvin.smartorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class baseDatosLocal extends SQLiteOpenHelper {

    String Productos_seleccionados = "" +
            "CREATE TABLE pro_seleccionados (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT,precio TEXT,ingredientes TEXT,activo TEXT DEFAULT '1')";

    String ingredientes_seleccionados = ""+
            "CREATE TABLE ing_seleccionados ("+
            "id INTEGER PRIMARY KEY AUTOINCREMENT,ingrediente TEXT,estado TEXT DEFAULT '1',id_pro INTEGER)";

    String usuario_actual = ""+
            "CREATE TABLE usuario(" +
            "usuario TEXT PRIMARY KEY, nombre TEXT, contrasena TEXT)";

    public baseDatosLocal(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Productos_seleccionados);
        db.execSQL(ingredientes_seleccionados);
        db.execSQL(usuario_actual);
        Log.d("crear","Creando base de datos");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS pro_seleccionados");
        db.execSQL(Productos_seleccionados);
        db.execSQL("DROP TABLE IF EXISTS ing_seleccionados");
        db.execSQL(ingredientes_seleccionados);
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL(usuario_actual);
        Log.d("actualizar","Actualizando base de datos");
    }
}
