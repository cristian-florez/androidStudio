package com.sena.mialedsandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sena.mialedsandroid.Config.DBConfig;
import com.sena.mialedsandroid.Controllers.ProductoDB;
import com.sena.mialedsandroid.Models.Producto;

public class EditarProducto extends AppCompatActivity {

    // Declaración de variables globales para los elementos de la vista
    private EditText inputNombre;
    private EditText inputPresentacion;
    private EditText inputPrecioCompra;
    private EditText inputPrecioVenta;
    private EditText inputCantidad;

    private Button btnAceptar;
    private Button btnCancelar;

    // Declaración de variables globales para la base de datos y el ID del producto
    private ProductoDB productoDB;
    private int productoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_producto);

        init();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método para inicializar los elementos de la vista
    private void init(){
        //desde el intent que enviamos desde inventario, se obtienen los datos del producto a editar
        //se obtiene el id del producto, el -1 es un valor por defecto en caso de que no se reciba el id
        productoId = getIntent().getIntExtra("producto_id", -1);
        String nombre = getIntent().getStringExtra("nombre");
        String presentacion = getIntent().getStringExtra("presentacion");
        String precioCompra = String.valueOf(getIntent().getIntExtra("precio_compra", 0));
        String precioVenta = String.valueOf(getIntent().getIntExtra("precio_venta", 0));
        String cantidad = String.valueOf(getIntent().getIntExtra("cantidad", 0));

        //se inicializan los elementos del formulario
        inputNombre = findViewById(R.id.inputEditarNombre);
        inputPresentacion = findViewById(R.id.inputEditarPresentacion);
        inputPrecioCompra = findViewById(R.id.inputEditarPrecioCompra);
        inputPrecioVenta = findViewById(R.id.inputEditarPrecioVenta);
        inputCantidad = findViewById(R.id.inputEditarCantidad);

        btnAceptar = findViewById(R.id.btnEditarAceptar);
        btnCancelar = findViewById(R.id.btnNuevoCancelar);

        //se inicializa la base de datos, se le pasa el contexto, el nombre de la base de datos y la versión
        productoDB = new ProductoDB(this, DBConfig.nombre, null, 1);

        //se asignan los valores del producto a los elementos del formulario
        inputNombre.setText(nombre);
        inputPresentacion.setText(presentacion);
        inputPrecioCompra.setText(precioCompra);
        inputPrecioVenta.setText(precioVenta);
        inputCantidad.setText(cantidad);

        //se asignan los eventos a los botones
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devolver();
            }
        });

    }

    // Método para guardar los cambios en el producto
    private void guardar(){
        //verificamos si se encontro el id del producto
        if (productoId == -1) {
            Log.d("TAG", "ID de producto no válido, no se puede actualizar.");
            return;
        }
        //se obtienen los valores de los elementos del formulario, usando el método trim() para eliminar los espacios en blanco y toString() para convertir a cadena
        String Nnombre = inputNombre.getText().toString().trim();
        String Npresentacion = inputPresentacion.getText().toString().trim();
        int NprecioCompra = Integer.parseInt(inputPrecioCompra.getText().toString().trim());
        int NprecioVenta = Integer.parseInt(inputPrecioVenta.getText().toString().trim());
        int Ncantidad = Integer.parseInt(inputCantidad.getText().toString().trim());

        //creammos un objeto de tipo Producto y le asignamos los valores obtenidos
        Producto producto = new Producto();
        producto.setNombre(Nnombre);
        producto.setPresentacion(Npresentacion);
        producto.setPrecio_compra(NprecioCompra);
        producto.setPrecio_venta(NprecioVenta);
        producto.setCantidad(Ncantidad);

        //llamamos el metodo actualizar de la base de datos y le pasamos el id del producto y el objeto producto creado
        productoDB.actualizar(productoId, producto);
        Log.d("TAG", "Producto actualizado con éxito: " + producto.getNombre());

        //devolvemos al inventario
        devolver();

    }

    // Método para devolver al inventario
    private void devolver(){
        Intent intent = new Intent(this, Inventario.class);
        startActivity(intent);
    }
}