package com.sena.mialedsandroid.Controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

// Clase que implementa la interfaz IUsuarioDB y extiende de SQLiteOpenHelper
public class UsuarioDB extends SQLiteOpenHelper implements IUsuarioDB {

    Context contexto;
    //constructor de la clase
    public UsuarioDB(@Nullable Context context,
                     @Nullable String nombre,
                     @Nullable SQLiteDatabase.CursorFactory factory,
                     int version) {

        super(context, nombre, factory, version);
        this.contexto = context;
    }

    //creamos la tabla usuario y insertamos un usuario por defecto
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE usuario(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "correo TEXT," +
                "clave TEXT)";
        sqLiteDatabase.execSQL(sql);
        String insert = "INSERT INTO usuario VALUES (null,'cristian', 'admin@gmail.com','123456')";
        sqLiteDatabase.execSQL(insert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //metodo para ingresar un usuario
    @Override
    public Boolean ingresar(String correo, String clave) {
        //declaramos las variables null para evitar errores
        SQLiteDatabase database = null;
        Cursor cursor = null;
        boolean resultado = false; // Variable para almacenar el resultado

        try {
            // Abre la base de datos en modo de solo lectura
            database = getReadableDatabase();

            // Consulta para verificar el usuario con los datos proporcionados
            String sql = "SELECT * FROM usuario WHERE correo = ? AND clave = ?";
            //el cursor almacena el resultado de la consulta
            cursor = database.rawQuery(sql, new String[]{correo, clave});

            // Si el cursor no está vacío, significa que se encontró el usuario
            resultado = cursor != null && cursor.moveToNext();
        } catch (Exception e) {
            Log.d("TAG", "Error al ingresar en usuarioDB: " + e.getMessage());
        } finally {
            // Cerramos el cursor solo si no es nulo ya que puede lanzar una excepción
            if (cursor != null) {
                cursor.close();
            }
            // Cerramos la base de datos solo si no es nulo y que la base de datos esté abierta para evitar excepciones, si no está abierta no se puede cerrar, asi se evita un error al momento de iniciar sesion por segunda vez
            if (database != null && database.isOpen()) {
                database.close();
            }
        }

        return resultado;
    }

}