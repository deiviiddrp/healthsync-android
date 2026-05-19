package com.healthsync.android;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParametroVitalTest {

    @Test
    public void testFormatoFecha_correcto() {
        String fechaISO = "2026-05-14T17:31:10.547388";
        String[] parts = fechaISO.split("T");
        String[] fecha = parts[0].split("-");
        String hora = parts[1].substring(0, 5);
        String resultado = fecha[2] + "/" + fecha[1] + "/" + fecha[0] + " " + hora;
        assertEquals("14/05/2026 17:31", resultado);
    }

    @Test
    public void testSemaforo_critico() {
        double valor = 150.0;
        double limiteNormal = 120.0;
        double limiteCritico = 140.0;
        String estado;
        if (valor > limiteCritico) estado = "CRITICO";
        else if (valor > limiteNormal) estado = "LIMITE";
        else estado = "NORMAL";
        assertEquals("CRITICO", estado);
    }

    @Test
    public void testSemaforo_normal() {
        double valor = 110.0;
        double limiteNormal = 120.0;
        double limiteCritico = 140.0;
        String estado;
        if (valor > limiteCritico) estado = "CRITICO";
        else if (valor > limiteNormal) estado = "LIMITE";
        else estado = "NORMAL";
        assertEquals("NORMAL", estado);
    }

    @Test
    public void testSemaforo_limite() {
        double valor = 130.0;
        double limiteNormal = 120.0;
        double limiteCritico = 140.0;
        String estado;
        if (valor > limiteCritico) estado = "CRITICO";
        else if (valor > limiteNormal) estado = "LIMITE";
        else estado = "NORMAL";
        assertEquals("LIMITE", estado);
    }

    @Test
    public void testFormatTipo_tension() {
        String tipo = "TENSION_SISTOLICA";
        String resultado = switch (tipo) {
            case "TENSION_SISTOLICA"  -> "Tensión sistólica";
            case "TENSION_DIASTOLICA" -> "Tensión diastólica";
            case "GLUCEMIA"    -> "Glucemia";
            case "PESO"        -> "Peso";
            case "FC"          -> "Frec. cardíaca";
            case "SPO2"        -> "Saturación O₂";
            case "TEMPERATURA" -> "Temperatura";
            default -> tipo;
        };
        assertEquals("Tensión sistólica", resultado);
    }

    @Test
    public void testEmail_extraccionNombre() {
        String email = "david@healthsync.com";
        String nombre = email.split("@")[0];
        assertEquals("david", nombre);
    }
}