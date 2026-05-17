package com.healthsync.android.model;

public class AlertaRequest {
    private String tipoParametro;
    private String operador;
    private double valorUmbral;
    private String mensajePersonalizado;

    public AlertaRequest(String tipoParametro, String operador,
                         double valorUmbral, String mensajePersonalizado) {
        this.tipoParametro = tipoParametro;
        this.operador = operador;
        this.valorUmbral = valorUmbral;
        this.mensajePersonalizado = mensajePersonalizado;
    }
}