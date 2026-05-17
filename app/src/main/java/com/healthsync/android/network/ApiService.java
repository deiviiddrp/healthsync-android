package com.healthsync.android.network;

import com.healthsync.android.model.*;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface ApiService {

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<UserResponse> register(@Body RegisterRequest request);

    @POST("auth/refresh")
    Call<AuthResponse> refreshToken(@Body RefreshTokenRequest request);

    @POST("parametros-vitales/registro")
    Call<ParametroVitalResponse> registrarMedicion(
            @Body ParametroVitalRequest request);

    @GET("parametros-vitales/historial")
    Call<PageResponse<ParametroVitalResponse>> getHistorial(
            @Query("page") int page,
            @Query("size") int size);

    @DELETE("parametros-vitales/{id}")
    Call<Void> eliminarMedicion(@Path("id") long id);

    @GET("citas")
    Call<List<CitaMedicaResponse>> getCitas(
            @Query("estado") String estado);

    @POST("citas")
    Call<CitaMedicaResponse> crearCita(@Body CitaMedicaRequest request);

    @DELETE("citas/{id}")
    Call<Void> eliminarCita(@Path("id") long id);

    @GET("alertas")
    Call<List<AlertaResponse>> getAlertas();

    @POST("alertas")
    Call<AlertaResponse> crearAlerta(@Body AlertaRequest request);

    @DELETE("alertas/{id}")
    Call<Void> eliminarAlerta(@Path("id") long id);
}