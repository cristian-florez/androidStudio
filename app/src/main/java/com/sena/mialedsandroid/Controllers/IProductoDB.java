package com.sena.mialedsandroid.Controllers;

import com.sena.mialedsandroid.Models.Producto;

import java.util.List;

/*se crea la interfaz IProductoDB para poder implementar los metodos de la clase ProductoDB
al crear esta interfaz se separa la logica de la base de datos de la logica de negocio*/
public interface IProductoDB {

    List<Producto> lista();

    List<Producto> buscar(String nombre);

    void agregar(Producto producto);

    void actualizar(Integer id, Producto producto);

    void eliminar(Integer eliminar);
}
