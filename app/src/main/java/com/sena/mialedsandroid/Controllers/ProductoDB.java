package com.sena.mialedsandroid.Controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sena.mialedsandroid.Models.Producto;

import java.util.ArrayList;
import java.util.List;

//esta clase se encarga de la conexion con la base de datos usando SQLite y implementa la interfaz IProductoDB
public class ProductoDB extends SQLiteOpenHelper implements IProductoDB {

    //generamos una variable statica para el nombre de la base de datos para evitar errores en la base de datos
    private static final String DATABASE_NAME = "mi_base_datos";
    //generamos una variable con la version de la base de datos, es 2 ya que se ha modificado la base de datos
    private static final int DATABASE_VERSION = 2;
    //generamos una variable de tipo Context que se encargara de manejar el contexto de la aplicacion (contexto se refiere a la actividad o fragmento que se esta ejecutando)
    private Context contexto;

    //los parametros que reciben el constructor son el contexto, el nombre de la base de datos, el factory y la version de la base de datos, contructor heredado de SQLiteOpenHelper
    public ProductoDB(@Nullable Context context, @Nullable String nombre, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.contexto = context;
    }

    //metodo que se encarga de crear la tabla producto en la base de datos
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE producto(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "presentacion TEXT, " +
                "precio_compra INTEGER, " +
                "precio_venta INTEGER, " +
                "cantidad INTEGER)";
        sqLiteDatabase.execSQL(sql);

        // Inserta algunos productos de prueba
        String insert = "INSERT INTO producto VALUES (null, 'mobil', 'litro', 25000, 28000, 5)";
        sqLiteDatabase.execSQL(insert);
        insert = "INSERT INTO producto VALUES (null, 'terpel', 'litro', 28000, 30000, 7)";
        sqLiteDatabase.execSQL(insert);
    }

    //metodo que se encarga de actualizar la base de datos, si la version de la base de datos cambia, se elimina la tabla antigua y se crea una nueva
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Elimina la tabla antigua si existe y crea una nueva
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS producto");
        onCreate(sqLiteDatabase);
    }

    //metodo que se encarga de extraer los productos de la base de datos, al usar un cursor se recorre la base de datos y se extraen los datos
    private Producto extraerProducto(Cursor cursor){
        Producto producto = new Producto();
        producto.setId(cursor.getInt(0));
        producto.setNombre(cursor.getString(1));
        producto.setPresentacion(cursor.getString(2));
        producto.setPrecio_compra(cursor.getInt(3));
        producto.setPrecio_venta(cursor.getInt(4));
        producto.setCantidad(cursor.getInt(5));
        return producto;
    }


    //metodo que se encarga de listar los productos de la base de datos para nuestra tabla, se crea una lista de productos y se recorre la base de datos para extraer los productos
    @Override
    public List<Producto> lista() {
        //se obtiene la base de datos en modo lectura
        SQLiteDatabase database = getReadableDatabase();
        //se crea una lista de productos vacia donde iremos agregando los productos
        List<Producto> productos = new ArrayList<>();
        //se crea una consulta sql para obtener todos los productos de la base de datos
        String sql = "SELECT * FROM producto";
        //se crea un cursor que se encargara de recorrer la base de datos, utilizamos el metodo rawQuery para ejecutar la consulta sql
        Cursor cursor = database.rawQuery(sql, null);
        //mientras el cursor tenga datos, se recorre la base de datos y se extraen los productos
        try {
            while (cursor.moveToNext()) {
                productos.add(extraerProducto(cursor));
            }
            //si ocurre un error se captura y se muestra un mensaje
        } catch (Exception e) {
            Log.d("TAG", "Error al obtener la lista de productos: " + e.getMessage());
        } finally {
            //se cierra el cursor, importante cerrar el cursor para evitar errores
            if (cursor != null) cursor.close();
        }
        return productos;
    }

    //metodo utilizado para buscar los productos por su nombre, con que contenga una parte del nombre se mostrara en la tabla
    @Override
    public List<Producto> buscar(String nombre) {
        //utilizamos la misma logica que en el metodo lista, lo unico que cambia es la consulta sql
        SQLiteDatabase database = getReadableDatabase();
        List<Producto> productos = new ArrayList<>();
        //la consulta sql nos permite buscar los productos que contengan una parte del nombre
        String sql = "SELECT * FROM producto WHERE nombre LIKE ?";
        //se crea un array de strings con el nombre del producto que se busca, se utiliza el simbolo % para buscar cualquier parte del nombre
        Cursor cursor = database.rawQuery(sql, new String[]{"%" + nombre + "%"});
        try {
            while (cursor.moveToNext()) {
                productos.add(extraerProducto(cursor));
            }
        } catch (Exception e) {
            Log.d("TAG", "Error al buscar productos por nombre: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return productos;
    }

    //metodo que agrega un producto a la base de datos, se utiliza un objeto de tipo Producto para agregarlo a la base de datos ya que vamos obteniendo cada atributo del producto, no necesita un id ya que es autoincrementable
    @Override
    public void agregar(Producto producto) {
        //esta vez se obtiene la base de datos en modo escritura
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO producto (nombre, presentacion, precio_compra, precio_venta, cantidad) VALUES (?, ?, ?, ?, ?)";
        database.execSQL(sql, new Object[]{producto.getNombre(), producto.getPresentacion(), producto.getPrecio_compra(), producto.getPrecio_venta(), producto.getCantidad()});
    }

    //el metodo para actualizar si necesita un id para buscar el producto en especifico y se modificara de acuerdo al objeto que se pasa como parametro
    @Override
    public void actualizar(Integer id, Producto producto) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE producto SET nombre = ?, presentacion = ?, precio_compra = ?, precio_venta = ?, cantidad = ? WHERE id = ?";
        database.execSQL(sql, new Object[]{producto.getNombre(), producto.getPresentacion(), producto.getPrecio_compra(), producto.getPrecio_venta(), producto.getCantidad(), id});
    }

    //el metodo para eliminar necesita un id para buscar el producto en especifico y eliminarlo
    @Override
    public void eliminar(Integer id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM producto WHERE id = ?";
        try {
            database.execSQL(sql, new Object[]{id});
            Log.d("TAG", "Producto eliminado con éxito, ID: " + id);
        } catch (Exception e) {
            Log.d("TAG", "Error al eliminar el producto: " + e.getMessage());
            throw e;
        }finally {
            database.close();
        }
    }
}
