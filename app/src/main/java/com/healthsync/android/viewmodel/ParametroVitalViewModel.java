package com.healthsync.android.viewmodel;

import androidx.lifecycle.*;
import com.healthsync.android.model.*;
import com.healthsync.android.repository.ParametroVitalRepository;
import java.util.List;

public class ParametroVitalViewModel extends ViewModel {

    private final ParametroVitalRepository repository;
    private final MutableLiveData<List<ParametroVitalResponse>> mediciones = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> registroExitoso = new MutableLiveData<>();

    public ParametroVitalViewModel(ParametroVitalRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<ParametroVitalResponse>> getMediciones() { return mediciones; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<Boolean> getRegistroExitoso() { return registroExitoso; }

    public void cargarHistorial() {
        loading.setValue(true);
        repository.getHistorial(0, 50,
                new ParametroVitalRepository.Callback<List<ParametroVitalResponse>>() {
                    @Override
                    public void onSuccess(List<ParametroVitalResponse> data) {
                        mediciones.postValue(data);
                        loading.postValue(false);
                    }
                    @Override
                    public void onError(String msg) {
                        error.postValue(msg);
                        loading.postValue(false);
                    }
                });
    }

    public void registrarMedicion(ParametroVitalRequest request) {
        loading.setValue(true);
        repository.registrar(request,
                new ParametroVitalRepository.Callback<ParametroVitalResponse>() {
                    @Override
                    public void onSuccess(ParametroVitalResponse data) {
                        registroExitoso.postValue(true);
                        loading.postValue(false);
                        cargarHistorial();
                    }
                    @Override
                    public void onError(String msg) {
                        error.postValue(msg);
                        loading.postValue(false);
                    }
                });
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final ParametroVitalRepository repo;
        public Factory(ParametroVitalRepository repo) { this.repo = repo; }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new ParametroVitalViewModel(repo);
        }
    }
}