package com.sena.mialedsandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sena.mialedsandroid.Controllers.UsuarioDB;

public class MainActivity extends AppCompatActivity {


    // Variables
    private EditText usuarioText;
    private EditText claveText;
    // Base de datos
    private final UsuarioDB usuarioDB = new UsuarioDB(this, "mi_base_datos", null, 1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void init(){
        // Inicializar variables de los inputs del formulario
        usuarioText = findViewById(R.id.correoUsuario);
        claveText = findViewById(R.id.claveUsuario);
    }

    //metodo para ingresar
    public void ingresar(View view) {
        // Obtener los valores de los inputs
        String correo = usuarioText.getText().toString();
        String clave = claveText.getText().toString();

        //en esta variale se guarda si el ingreso fue exitoso
        boolean ingresoExitoso = usuarioDB.ingresar(correo, clave);

        // Si el ingreso fue exitoso, redirigir al inventario, caso contrario mostrar un mensaje
        if (ingresoExitoso) {
            Intent intent = new Intent(this, Inventario.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Correo o clave incorrecta", Toast.LENGTH_SHORT).show();
        }
    }
}