package com.sena.mialedsandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sena.mialedsandroid.Config.DBConfig;
import com.sena.mialedsandroid.Controllers.ProductoDB;
import com.sena.mialedsandroid.Models.Producto;

public class NuevoProducto extends AppCompatActivity {

    // Variables
    private EditText inputNombre;
    private EditText inputPresentacion;
    private EditText inputPrecioCompra;
    private EditText inputPrecioVenta;
    private EditText inputCantidad;

    private Button btnAgregar;
    private Button btnCancelar;


    private ProductoDB productoDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_producto);

        init();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void init(){
        // Inicializar variables
        inputNombre = findViewById(R.id.inputNuevoNombre);
        inputPresentacion = findViewById(R.id.inputNuevoPresentacion);
        inputPrecioCompra = findViewById(R.id.inputNuevoPrecioCompra);
        inputPrecioVenta = findViewById(R.id.inputNuevoPrecioVenta);
        inputCantidad = findViewById(R.id.inputNuevoCantidad);

        btnAgregar = findViewById(R.id.btnNuevoAgregar);
        btnCancelar = findViewById(R.id.btnNuevoCancelar);


        // Inicializar base de datos
        productoDB = new ProductoDB(this, DBConfig.nombre, null, 1);

        // Eventos de botones
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarProducto();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devolver();
            }
        });
    }

    //metodo para insertar un producto
    private void insertarProducto(){
        // Obtener valores de los inputs
        String nombre = inputNombre.getText().toString().trim();
        String presentacion = inputPresentacion.getText().toString().trim();
        int precioCompra = Integer.parseInt(inputPrecioCompra.getText().toString().trim());
        int precioVenta = Integer.parseInt(inputPrecioVenta.getText().toString().trim());
        int cantidad = Integer.parseInt(inputCantidad.getText().toString().trim());

        // Crear objeto de producto vacio y vamos a llenarlo con los valores de los inputs
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(nombre);
        nuevoProducto.setPresentacion(presentacion);
        nuevoProducto.setPrecio_compra(precioCompra);
        nuevoProducto.setPrecio_venta(precioVenta);
        nuevoProducto.setCantidad(cantidad);

        // Agregar el producto a la base de datos
        productoDB.agregar(nuevoProducto);

        // Mostrar mensaje de exito
        Toast.makeText(this, "Producto guardado exitosamente", Toast.LENGTH_SHORT).show();

        // Limpiar los inputs
        inputNombre.setText("");
        inputPresentacion.setText("");
        inputPrecioCompra.setText("");
        inputPrecioVenta.setText("");
        inputCantidad.setText("");

    }

    //metodo para devolver a inventario al dar clic en cancelar
    private void devolver(){
        Intent intent = new Intent(this, Inventario.class);
        startActivity(intent);
    }
}
