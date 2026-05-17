package com.healthsync.android.repository;

import android.content.Context;
import com.healthsync.android.model.*;
import com.healthsync.android.network.RetrofitClient;
import com.healthsync.android.network.ApiService;
import com.healthsync.android.util.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class ParametroVitalRepository {

    public interface Callback<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    private final ApiService apiService;

    public ParametroVitalRepository(SessionManager session, Context context) {
        apiService = RetrofitClient.getInstance(session).getApiService();
    }

    public void getHistorial(int page, int size,
                             Callback<List<ParametroVitalResponse>> callback) {
        apiService.getHistorial(page, size)
                .enqueue(new retrofit2.Callback<PageResponse<ParametroVitalResponse>>() {
                    @Override
                    public void onResponse(Call<PageResponse<ParametroVitalResponse>> call,
                                           Response<PageResponse<ParametroVitalResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body().getContent());
                        } else {
                            callback.onError("Error: " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<PageResponse<ParametroVitalResponse>> call,
                                          Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void registrar(ParametroVitalRequest request,
                          Callback<ParametroVitalResponse> callback) {
        apiService.registrarMedicion(request)
                .enqueue(new retrofit2.Callback<ParametroVitalResponse>() {
                    @Override
                    public void onResponse(Call<ParametroVitalResponse> call,
                                           Response<ParametroVitalResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Error: " + response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<ParametroVitalResponse> call,
                                          Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }
}