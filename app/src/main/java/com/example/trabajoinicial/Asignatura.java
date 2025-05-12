package com.example.trabajoinicial;

public class Asignatura {
    private String nombre;
    private double nota;

    public Asignatura(String nombre, double nota) {
        this.nombre = nombre;
        this.nota = nota;
    }

    public String getNombre() {
        return nombre;
    }

    public double getNota() {
        return nota;
    }
}
