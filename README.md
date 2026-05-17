# HealthSync — Android

Aplicación móvil Android para la gestión integral de la salud personal. Forma parte del Proyecto Intermodular del Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multimedia (DAM).

## Tecnologías

- Java 17
- Android SDK 34 (mínimo API 26)
- MVVM + Repository pattern
- Material Design 3
- Retrofit 2 + OkHttp
- Room (base de datos local)
- Android Navigation Component
- MPAndroidChart

## Funcionalidades

- Login y registro con autenticación JWT
- Dashboard con resumen de últimas mediciones
- Semáforo de salud (verde/amarillo/rojo)
- Historial de parámetros vitales
- Registro de tensión, glucemia, peso, frecuencia cardíaca, saturación O₂ y temperatura
- Gestión de citas médicas
- Alertas personalizadas de salud
- Cierre de sesión

## Configuración

En build.gradle.kts ajusta la URL del backend:
- Emulador: http://10.0.2.2:8080/api/v1/
- Producción: https://TU_BACKEND/api/v1/

## Autor

**David Rodríguez Palomeque**
Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multimedia
CEFP-UCJC · Curso 2025-2026
