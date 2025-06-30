package com.example.pm2e1390415.Modelos;

public class Pais {
    private final String nombre;
    private final String codigo;
    private final String regex;

    public Pais(String nombre, String codigo, String regex) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.regex = regex;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getRegex() {
        return regex;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

