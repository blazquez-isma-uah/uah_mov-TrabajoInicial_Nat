package com.example.trabajoinicial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Entradas de datos
    EditText entradaAsignatura, entradaNota;
    // Botones
    Button botonGuardar, botonConsultar;

    // Lista de asignaturas
    List<Asignatura> asignaturas = new ArrayList<>();

    // Preferencias compartidas para guardar datos
    SharedPreferences sharedPreferences;
    // Gson para convertir objetos a JSON
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializar widgets
        entradaAsignatura = findViewById(R.id.entradaAsignatura);
        entradaNota = findViewById(R.id.entradaNota);
        botonGuardar = findViewById(R.id.botonGuardar);
        botonConsultar = findViewById(R.id.botonConsultar);

        // Recuperar preferencias compartidas
        sharedPreferences = getSharedPreferences("asignaturasPreferences", MODE_PRIVATE);

        // Recuperar la lista de asignaturas guardadas
        String jsonGuardado = sharedPreferences.getString("asignaturas", null);
        if (jsonGuardado != null) {
            // Convertir el JSON a una lista de objetos Asignatura
            Type tipoLista = new TypeToken<List<Asignatura>>() {}.getType();
            asignaturas = gson.fromJson(jsonGuardado, tipoLista);
        }

        // Comportamiento del botón "Guardar"
        botonGuardar.setOnClickListener(v -> guardarAsignatura());

        // Comportamiento del botón "Consultar"
        botonConsultar.setOnClickListener(v -> {
            // Ir a la Activity de consulta
            Intent intent = new Intent(MainActivity.this, ShowActivity.class);
            startActivity(intent);
        });

    }

    private void guardarAsignatura() {
        String nombreAsignatura = entradaAsignatura.getText().toString();
        String notaString = entradaNota.getText().toString();

        // Validar entradas
        if (nombreAsignatura.isEmpty() || notaString.isEmpty()) {
            Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que la asignatura no existe
        for (Asignatura asignatura : asignaturas) {
            if (asignatura.getNombre().equalsIgnoreCase(nombreAsignatura)) {
                Toast.makeText(this, "La asignatura ya existe", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        try {
            // Validar que la nota es un número
            double nota = Double.parseDouble(notaString);

            // Crear objeto Asignatura
            Asignatura asignatura = new Asignatura(nombreAsignatura, nota);
            asignaturas.add(asignatura);

            // Guardar la lista de asignaturas en preferencias compartidas
            String jsonGuardado = gson.toJson(asignaturas);
            sharedPreferences.edit().putString("asignaturas", jsonGuardado).apply();

            Toast.makeText(this, "Asignatura guardada", Toast.LENGTH_SHORT).show();

            // Limpiar entradas
            entradaAsignatura.setText("");
            entradaNota.setText("");
        } catch (NumberFormatException e) {
            Toast.makeText(this, "La nota debe ser un número", Toast.LENGTH_SHORT).show();
        }
    }
}