package com.example.irvin.smartorder;

public class producto {

    private String id,nombre,precio,ingredientes,posicion;

    public producto(String id,String nombre, String precio) {
        this.nombre = nombre;
        this.precio = precio;
        this.id = id;
    }
    public producto(String id,String nombre, String precio, String ingredientes) {
        this.nombre = nombre;
        this.precio = precio;
        this.id = id;
        this.ingredientes = ingredientes;
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }
}
