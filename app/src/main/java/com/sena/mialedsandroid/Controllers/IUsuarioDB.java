package com.sena.mialedsandroid.Controllers;

import com.sena.mialedsandroid.Models.Usuario;

import java.util.List;

//esta interfaz solo tiene un metodo que es ingresar que manejara la autenticacion de los usuarios
public interface IUsuarioDB {


    Boolean ingresar(String correo, String clave);

}
