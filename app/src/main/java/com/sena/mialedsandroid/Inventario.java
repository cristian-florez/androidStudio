package com.sena.mialedsandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sena.mialedsandroid.Config.DBConfig;
import com.sena.mialedsandroid.Controllers.ProductoDB;
import com.sena.mialedsandroid.Models.Producto;

import java.util.List;

public class Inventario extends AppCompatActivity {

    // Declaración de variables
    private TableLayout tabla;

    private EditText inputBuscar;
    private Button btnBuscar;

    private  Button btnEditarProducto;
    private Button btnNuevoProducto;
    private Button btnEliminarProducto;
    private Button btnSalir;

    //variables para manejar la logica de los botones
    private boolean selecionEditar = false;
    private boolean selecionEliminar = false;


    private ProductoDB productoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventario);

        productoDB = new ProductoDB(this, DBConfig.nombre, null, 1); // Inicialización del objeto productoDB

        init();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void init() {

        cargarProductos();

        // Inicialización de variables para la búsqueda
        inputBuscar = findViewById(R.id.inputBuscar);
        btnBuscar = findViewById(R.id.btnBuscar);

        // Evento para el botón de búsqueda
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el texto de búsqueda
                String query = inputBuscar.getText().toString().trim();
                // Verificar si la búsqueda no está vacía, si es así, se cargan todos los productos
                if (!query.isEmpty()) {
                    buscarProducto(query);
                } else {
                    cargarProductos();
                }
                //manejamos este metodo para habilitar algun boton de editar o eliminar si esta desactivado
                reiniciarBotones();
            }
        });

        // Inicialización de variables para los botones
        btnNuevoProducto = findViewById(R.id.btnNuevoProducto);
        btnEditarProducto = findViewById(R.id.btnEditarProducto);
        btnEliminarProducto = findViewById(R.id.btnEliminarProducto);
        btnSalir = findViewById(R.id.btnSalir);


        // Eventos para los botones
        btnNuevoProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irNuevoProducto();
                reiniciarBotones();
            }
        });

        btnEditarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cambiamos el estado de selecionEditar a true para al momento de dar clic en una fila sepa que debe editar
                selecionEditar = true;
                //deshabilitamos el boton de editar y habilitamos el de eliminar si esta deshabilitado
                btnEditarProducto.setEnabled(false);
                btnEliminarProducto.setEnabled(true);
            }
        });

        btnEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cambiamos el estado de selecionEliminar a true para al momento de dar clic en una fila sepa que debe eliminar
                selecionEliminar = true;
                //deshabilitamos el boton de eliminar y habilitamos el de editar si esta deshabilitado
                btnEliminarProducto.setEnabled(false);
                btnEditarProducto.setEnabled(true);
            }
        });

        // Evento para el botón de salir y volver a iniciar sesion
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reiniciarBotones();
                salir();
            }
        });

    }

    //metodo para cargar los productos en la tabla, este metodo solo se encarga de cargar los productos, no de mostrarlos
    private void cargarProductos(){
        tabla = findViewById(R.id.tabla_productos);
        List<Producto> productoList = productoDB.lista();
        mostrarProductos(productoList);
    }

    //metodo para mostrar los productos en la tabla, se separa de la logica de cargar productos para poder reutilizarlo y evitar el error de no cargar los productos despues de editarlos
    private void mostrarProductos(List<Producto> productoList) {
        // Limpiar la tabla para evitar que se dupliquen los productos
        tabla.removeAllViews();
        //recorremos la lista de productos que pasamos por parametros y los mostramos en la tabla
        for (Producto producto : productoList) {
            // Crear una fila para cada producto
            TableRow fila = new TableRow(this);

            //creamos una celda para cada dato del producto
            TextView nombreText = new TextView(this);
            nombreText.setText(producto.getNombre());
            nombreText.setPadding(8, 8, 8, 8);

            TextView presentacionText = new TextView(this);
            presentacionText.setText(producto.getPresentacion());
            presentacionText.setPadding(8, 8, 8, 8);

            TextView precioText = new TextView(this);
            precioText.setText(String.valueOf(producto.getPrecio_venta())); // Conversión a String
            precioText.setPadding(8, 8, 8, 8);

            TextView cantidadText = new TextView(this);
            cantidadText.setText(String.valueOf(producto.getCantidad())); // Conversión a String
            cantidadText.setPadding(8, 8, 8, 8);

            // Agregar los TextView a la fila
            fila.addView(nombreText);
            fila.addView(presentacionText);
            fila.addView(precioText);
            fila.addView(cantidadText);

            fila.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selecionEditar) {
                        irEditarProducto(producto);
                    } else if(selecionEliminar){
                        avisoEliminar(producto.getNombre(), producto.getId());
                    }
                }
            });

            // Agregar la fila a la tabla
            tabla.addView(fila);
        }
    }

    //metodo para buscar productos por nombre
    private void buscarProducto(String query){
        tabla.removeAllViews();
        //esta lista tendra todos los productos que coincidan con la busqueda, si esta vacia mostrara un mensaje
        List<Producto> productosFiltrados = productoDB.buscar(query);
        if(productosFiltrados != null && !productosFiltrados.isEmpty()){
            mostrarProductos(productosFiltrados);
        } else {
            TextView sinResultados = new TextView(this);
            sinResultados.setText("no se encontraron productos");
            tabla.addView(sinResultados);
        }
    }

    //metodo para habilitar los botones de editar y eliminar, se llama cuando se da clic en una fila para evitar errores
    private void reiniciarBotones(){
        selecionEditar = false;
        selecionEliminar = false;
        btnEditarProducto.setEnabled(true);
        btnEliminarProducto.setEnabled(true);
    }

    //metodos para navegar entre las actividades
    private void salir(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void irNuevoProducto(){
        Intent intent = new Intent(this, NuevoProducto.class);
        startActivity(intent);
    }

    //metodo para ir a la actividad de editar producto, se le pasan los datos del producto que se quiere editar, los cuales se obtienen de la fila en la que se dio clic
    private void irEditarProducto(Producto producto){
        Intent intent = new Intent(this, EditarProducto.class);

        intent.putExtra("producto_id", producto.getId());
        intent.putExtra("nombre", producto.getNombre());
        intent.putExtra("presentacion", producto.getPresentacion());
        intent.putExtra("precio_compra", producto.getPrecio_compra());
        intent.putExtra("precio_venta", producto.getPrecio_venta());
        intent.putExtra("cantidad", producto.getCantidad());

        startActivity(intent);
    }


    //metodo para mostrar un aviso de confirmacion antes de eliminar un producto
    private void avisoEliminar(String nombre, int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Advertencia");
        builder.setMessage("¿estas seguro de eliminar el producto " + nombre + "?");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                productoDB.eliminar(id);
                List<Producto> productoLista= productoDB.lista();
                dialog.dismiss();
                mostrarProductos(productoLista);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        reiniciarBotones();
    }
}
