package com.sena.mialedsandroid.Models;

public class Producto {

    private Integer id;
    private String nombre;
    private String presentacion;
    private Integer precio_compra;
    private Integer precio_venta;
    private Integer cantidad;

    public Producto(){

    }

    public Producto(Integer id, String nombre, String presentacion, Integer precio_compra, Integer precio_venta, Integer cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.presentacion = presentacion;
        this.precio_compra = precio_compra;
        this.precio_venta = precio_venta;
        this.cantidad = cantidad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public Integer getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(Integer precio_compra) {
        this.precio_compra = precio_compra;
    }

    public Integer getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(Integer precio_venta) {
        this.precio_venta = precio_venta;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}

