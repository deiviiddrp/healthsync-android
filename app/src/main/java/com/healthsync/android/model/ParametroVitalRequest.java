package com.healthsync.android.model;

public class ParametroVitalRequest {
    private String tipoParametro;
    private double valor;
    private String unidad;
    private String notas;

    public ParametroVitalRequest(String tipoParametro, double valor,
                                 String unidad, String notas) {
        this.tipoParametro = tipoParametro;
        this.valor = valor;
        this.unidad = unidad;
        this.notas = notas;
    }
}