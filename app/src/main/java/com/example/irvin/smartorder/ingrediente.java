package com.example.irvin.smartorder;

public class ingrediente {

    private int id;
    private String nombre;
    private boolean estado;
    private int id_pro;

    public ingrediente(int id, String nombre, boolean estado, int id_pro) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.id_pro = id_pro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEstado() {
        return estado;
    }

    public int getId_pro() {
        return id_pro;
    }

    public void setId_pro(int id_pro) {
        this.id_pro = id_pro;
    }
}
