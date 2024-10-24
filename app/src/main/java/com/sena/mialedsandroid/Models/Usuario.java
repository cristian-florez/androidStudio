package com.sena.mialedsandroid.Models;

public class Usuario {

    private Integer id;
    private String nombre;
    private String correo;
    private String clave;

    public Usuario(){

    }

    public Usuario(String nombre, Integer id, String correo, String clave) {
        this.nombre = nombre;
        this.id = id;
        this.correo = correo;
        this.clave = clave;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
