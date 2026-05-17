package com.healthsync.android.model;

public class AlertaResponse {
    private long id;
    private String tipoParametro;
    private String operador;
    private double valorUmbral;
    private boolean activa;
    private String mensajePersonalizado;

    public long getId() { return id; }
    public String getTipoParametro() { return tipoParametro; }
    public String getOperador() { return operador; }
    public double getValorUmbral() { return valorUmbral; }
    public boolean isActiva() { return activa; }
    public String getMensajePersonalizado() { return mensajePersonalizado; }
}