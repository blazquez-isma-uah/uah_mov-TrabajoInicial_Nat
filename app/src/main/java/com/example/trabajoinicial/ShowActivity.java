package com.example.trabajoinicial;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShowActivity extends AppCompatActivity {

    // RecyclerView para mostrar la lista de asignaturas
    RecyclerView recyclerView;
    // Adaptador para el RecyclerView
    AsignaturaAdapter adaptador;
    // Lista de asignaturas
    List<Asignatura> listaAsignaturas = new ArrayList<>();
    // Botones
    Button botonOrdenarNombre, botonOrdenarNota, botonVolver;

    // Variables para ordenar ascendente o descendente
    boolean ordenAscendenteNombre = true;
    boolean ordenAscendenteNota = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show);

        // Inicializar los botones
        botonOrdenarNombre = findViewById(R.id.botonOrdenarNombre);
        botonOrdenarNota = findViewById(R.id.botonOrdenarNota);
        botonVolver = findViewById(R.id.botonVolver);

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recyclerAsignaturas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener la lista de asignaturas de las preferencias compartidas
        SharedPreferences sharedPreferences = getSharedPreferences("asignaturasPreferences", MODE_PRIVATE);
        String jsonGuardado = sharedPreferences.getString("asignaturas", null);

        if (jsonGuardado != null && !jsonGuardado.isEmpty()) {
            // Convertir el JSON a una lista de objetos Asignatura
            listaAsignaturas = new Gson().fromJson(jsonGuardado, new TypeToken<List<Asignatura>>() {}.getType());
            ordenarPorNombre(); // Ordenar por nombre al inicio
        } else {
            // Si no hay datos guardados, mostrar un mensaje
            Toast.makeText(this, "No hay asignaturas guardadas", Toast.LENGTH_SHORT).show();
        }

        // Configurar el adaptador del RecyclerView
        adaptador = new AsignaturaAdapter(listaAsignaturas);
        recyclerView.setAdapter(adaptador);

        // Configurar los botones
        botonOrdenarNombre.setOnClickListener(v -> {
            ordenarPorNombre();
            adaptador.notifyDataSetChanged();
        });

        botonOrdenarNota.setOnClickListener(v -> {
            ordenarPorNota();
            adaptador.notifyDataSetChanged();
        });

        botonVolver.setOnClickListener(v -> {
            // Volver a la actividad anterior
            finish();
        });
    }

    private void ordenarPorNota() {
        String texto = getString(R.string.ordenar_por_nota);

        // Ordenar la lista por nota
        if(ordenAscendenteNota) {
            listaAsignaturas.sort(Comparator.comparing(Asignatura::getNota));
            texto = texto + " ↓";
        } else {
            listaAsignaturas.sort(Comparator.comparing(Asignatura::getNota).reversed());
            texto += " ↑";
        }
        botonOrdenarNota.setText(texto);
        // Se pone el texto del otro botón en su estado original
        botonOrdenarNombre.setText(getString(R.string.ordenar_por_nombre));
        ordenAscendenteNota = !ordenAscendenteNota; // Cambiar el orden para la próxima vez
    }

    private void ordenarPorNombre() {
        String texto = getString(R.string.ordenar_por_nombre);
        // Ordenar la lista por nombre según el orden actual
        if(ordenAscendenteNombre) {
            listaAsignaturas.sort((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
            texto += " ↓";
        } else {
            listaAsignaturas.sort((a, b) -> b.getNombre().compareToIgnoreCase(a.getNombre()));
            texto += " ↑";
        }
        botonOrdenarNombre.setText(texto);
        // Se pone el texto del otro botón en su estado original
        botonOrdenarNota.setText(getString(R.string.ordenar_por_nota));
        ordenAscendenteNombre = !ordenAscendenteNombre; // Cambiar el orden para la próxima vez
    }
}