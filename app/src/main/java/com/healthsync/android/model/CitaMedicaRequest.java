package com.healthsync.android.model;

public class CitaMedicaRequest {
    private String especialidad;
    private String nombreMedico;
    private String centroMedico;
    private String fechaHora;
    private String notas;

    public CitaMedicaRequest(String especialidad, String nombreMedico,
                             String centroMedico, String fechaHora, String notas) {
        this.especialidad = especialidad;
        this.nombreMedico = nombreMedico;
        this.centroMedico = centroMedico;
        this.fechaHora = fechaHora;
        this.notas = notas;
    }
}