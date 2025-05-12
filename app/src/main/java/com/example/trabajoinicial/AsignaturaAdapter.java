package com.example.trabajoinicial;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

/**
 * Clase Adapter para manejar la lista de asignaturas en un RecyclerView.
 * Esta clase es responsable de crear y enlazar las vistas de los elementos de la lista.
 */
public class AsignaturaAdapter extends RecyclerView.Adapter<AsignaturaAdapter.ViewHolder> {

    private List<Asignatura> listaAsignaturas;

    public AsignaturaAdapter(List<Asignatura> listaAsignaturas) {
        this.listaAsignaturas = listaAsignaturas;
    }

    // ViewHolder para mostrar cada elemento de la lista
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textoNombre, textoNota;
        public ViewHolder(View itemView) {
            super(itemView);
            textoNombre = itemView.findViewById(R.id.textoNombre);
            textoNota = itemView.findViewById(R.id.textoNota);
        }
    }

    @NonNull
    @Override
    public AsignaturaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Crear una nueva vista para cada elemento de la lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asignatura, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AsignaturaAdapter.ViewHolder holder, int position) {
        // Asignar los datos de la asignatura a las vistas
        Asignatura asignatura = listaAsignaturas.get(position);
        holder.textoNombre.setText(asignatura.getNombre());
        holder.textoNota.setText("Nota: " + asignatura.getNota());
        // Cambiar el color del texto según la nota
        if (asignatura.getNota() < 5) {
            holder.textoNota.setTextColor(Color.RED);
        } else if(asignatura.getNota() < 7) {
            holder.textoNota.setTextColor(Color.parseColor("#FFA500")); // Naranja
        } else {
            holder.textoNota.setTextColor(Color.parseColor("#228B22")); // Verde
        }

        // Conn un long click se eliminara la asignatura
        holder.itemView.setOnLongClickListener(v -> {
            int pos = holder.getAdapterPosition();
            Asignatura asignaturaEliminada = listaAsignaturas.get(pos);
            listaAsignaturas.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, listaAsignaturas.size());

            // Actualizar SharedPreferences
            SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("asignaturasPreferences", v.getContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("asignaturas", new Gson().toJson(listaAsignaturas));
            editor.apply();

            // Mostrar un mensaje de confirmación
            Toast.makeText(v.getContext(), "Asignatura eliminada: " + asignaturaEliminada.getNombre(), Toast.LENGTH_SHORT).show();
            return true; // Indica que el evento fue manejado
        });

    }

    @Override
    public int getItemCount() {
        return listaAsignaturas.size();
    }
}
