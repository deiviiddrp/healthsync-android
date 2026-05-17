package com.healthsync.android.model;

public class CitaMedicaResponse {
    private long id;
    private String especialidad;
    private String nombreMedico;
    private String centroMedico;
    private String fechaHora;
    private String estado;
    private String notas;

    public long getId() { return id; }
    public String getEspecialidad() { return especialidad; }
    public String getNombreMedico() { return nombreMedico; }
    public String getCentroMedico() { return centroMedico; }
    public String getFechaHora() { return fechaHora; }
    public String getEstado() { return estado; }
    public String getNotas() { return notas; }
}