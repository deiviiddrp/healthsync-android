package com.healthsync.android.model;

public class RegisterRequest {
    private String nombre;
    private String apellidos;
    private String email;
    private String password;

    public RegisterRequest(String nombre, String apellidos,
                           String email, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
    }
}