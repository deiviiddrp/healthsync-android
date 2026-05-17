package com.healthsync.android.ui;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.healthsync.android.adapter.AlertaAdapter;
import com.healthsync.android.databinding.FragmentAlertasBinding;
import com.healthsync.android.model.*;
import com.healthsync.android.network.RetrofitClient;
import com.healthsync.android.util.SessionManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertasFragment extends Fragment
        implements AlertaAdapter.OnAlertaListener {

    private FragmentAlertasBinding binding;
    private AlertaAdapter adapter;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlertasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        setupRecyclerView();
        setupFab();
        cargarAlertas();
    }

    private void setupRecyclerView() {
        adapter = new AlertaAdapter(this);
        binding.rvAlertas.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        binding.rvAlertas.setAdapter(adapter);
    }

    private void setupFab() {
        binding.fabNuevaAlerta.setOnClickListener(v -> mostrarDialogNuevaAlerta());
    }

    private void cargarAlertas() {
        RetrofitClient.getInstance(sessionManager).getApiService()
                .getAlertas()
                .enqueue(new Callback<List<AlertaResponse>>() {
                    @Override
                    public void onResponse(Call<List<AlertaResponse>> call,
                                           Response<List<AlertaResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<AlertaResponse> alertas = response.body();
                            adapter.submitList(alertas);
                            binding.tvEmpty.setVisibility(
                                    alertas.isEmpty() ? View.VISIBLE : View.GONE);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<AlertaResponse>> call, Throwable t) {
                        Toast.makeText(requireContext(),
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarDialogNuevaAlerta() {
        String[] tipos = {"TENSION_SISTOLICA", "GLUCEMIA", "PESO", "FC", "SPO2"};
        String[] etiquetas = {"Tensión sistólica", "Glucemia",
                "Peso", "Frec. cardíaca", "Saturación O₂"};
        String[] operadores = {">", ">=", "<", "<=", "="};

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Parámetro")
                .setItems(etiquetas, (d1, tipoIdx) ->
                        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                .setTitle("Condición")
                                .setItems(operadores, (d2, opIdx) -> {
                                    android.widget.EditText etValor =
                                            new android.widget.EditText(requireContext());
                                    etValor.setHint("Valor umbral");
                                    etValor.setInputType(
                                            android.text.InputType.TYPE_CLASS_NUMBER
                                                    | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                    new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                            .setTitle("Valor")
                                            .setView(etValor)
                                            .setPositiveButton("Crear", (d3, w) -> {
                                                String val = etValor.getText().toString();
                                                if (!val.isEmpty()) {
                                                    crearAlerta(new AlertaRequest(
                                                            tipos[tipoIdx],
                                                            operadores[opIdx],
                                                            Double.parseDouble(val), ""));
                                                }
                                            })
                                            .setNegativeButton("Cancelar", null)
                                            .show();
                                }).show())
                .show();
    }

    private void crearAlerta(AlertaRequest request) {
        RetrofitClient.getInstance(sessionManager).getApiService()
                .crearAlerta(request)
                .enqueue(new Callback<AlertaResponse>() {
                    @Override
                    public void onResponse(Call<AlertaResponse> call,
                                           Response<AlertaResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(),
                                    "Alerta creada", Toast.LENGTH_SHORT).show();
                            cargarAlertas();
                        }
                    }
                    @Override
                    public void onFailure(Call<AlertaResponse> call, Throwable t) {
                        Toast.makeText(requireContext(),
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onToggle(AlertaResponse alerta, boolean activa) {
        Toast.makeText(requireContext(),
                activa ? "Alerta activada" : "Alerta desactivada",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDelete(AlertaResponse alerta) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Eliminar alerta")
                .setMessage("¿Eliminar esta alerta?")
                .setPositiveButton("Eliminar", (d, w) ->
                        RetrofitClient.getInstance(sessionManager).getApiService()
                                .eliminarAlerta(alerta.getId())
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call,
                                                           Response<Void> response) {
                                        cargarAlertas();
                                    }
                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {}
                                }))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}