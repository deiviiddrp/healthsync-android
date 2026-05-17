package com.healthsync.android.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "parametro_vital_local")
public class ParametroVitalEntity {

    @PrimaryKey(autoGenerate = true)
    public long localId;
    public Long remoteId;
    public String tipoParametro;
    public double valor;
    public String unidad;
    public String fechaHora;
    public String estadoSemaforo;
    public String notas;
    public String syncStatus = "PENDING_SYNC";
}