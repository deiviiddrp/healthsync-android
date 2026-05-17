package com.healthsync.android.model;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;

public class ParametroVitalResponse {
    @SerializedName("id")
    private long id;
    @SerializedName("tipoParametro")
    private String tipoParametro;
    @SerializedName("valor")
    private double valor;
    @SerializedName("unidad")
    private String unidad;
    @SerializedName("estadoSemaforo")
    private String estadoSemaforo;
    @SerializedName("notas")
    private String notas;
    @SerializedName("fechaHora")
    private String fechaHora;

    public long getId() { return id; }
    public String getTipoParametro() { return tipoParametro; }
    public double getValor() { return valor; }
    public String getUnidad() { return unidad; }
    public String getEstadoSemaforo() { return estadoSemaforo; }
    public String getNotas() { return notas; }
    public String getFechaHora() { return fechaHora; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParametroVitalResponse)) return false;
        return id == ((ParametroVitalResponse) o).id;
    }

    @Override
    public int hashCode() { return Long.hashCode(id); }
}