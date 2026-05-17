package com.healthsync.android.ui;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.healthsync.android.adapter.CitaAdapter;
import com.healthsync.android.databinding.FragmentCitasBinding;
import com.healthsync.android.model.*;
import com.healthsync.android.network.RetrofitClient;
import com.healthsync.android.util.SessionManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitasFragment extends Fragment {

    private FragmentCitasBinding binding;
    private CitaAdapter adapter;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCitasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        setupRecyclerView();
        setupFab();
        cargarCitas();
    }

    private void setupRecyclerView() {
        adapter = new CitaAdapter();
        binding.rvCitas.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        binding.rvCitas.setAdapter(adapter);
    }

    private void setupFab() {
        binding.fabNuevaCita.setOnClickListener(v -> mostrarDialogNuevaCita());
    }

    private void cargarCitas() {
        binding.progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance(sessionManager).getApiService()
                .getCitas(null)
                .enqueue(new Callback<List<CitaMedicaResponse>>() {
                    @Override
                    public void onResponse(Call<List<CitaMedicaResponse>> call,
                                           Response<List<CitaMedicaResponse>> response) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            List<CitaMedicaResponse> citas = response.body();
                            adapter.submitList(citas);
                            binding.tvEmpty.setVisibility(
                                    citas.isEmpty() ? View.VISIBLE : View.GONE);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<CitaMedicaResponse>> call,
                                          Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(requireContext(),
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarDialogNuevaCita() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(android.R.layout.simple_list_item_2, null);

        android.widget.EditText etEspecialidad = new android.widget.EditText(requireContext());
        etEspecialidad.setHint("Especialidad (ej: Cardiología)");
        android.widget.EditText etMedico = new android.widget.EditText(requireContext());
        etMedico.setHint("Nombre del médico");
        android.widget.EditText etFecha = new android.widget.EditText(requireContext());
        etFecha.setHint("Fecha (ej: 2026-06-15T10:00:00)");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(48, 16, 48, 16);
        layout.addView(etEspecialidad);
        layout.addView(etMedico);
        layout.addView(etFecha);

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Nueva cita médica")
                .setView(layout)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String especialidad = etEspecialidad.getText().toString();
                    String medico = etMedico.getText().toString();
                    String fecha = etFecha.getText().toString();
                    if (!especialidad.isEmpty() && !fecha.isEmpty()) {
                        crearCita(new CitaMedicaRequest(
                                especialidad, medico, "", fecha, ""));
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void crearCita(CitaMedicaRequest request) {
        RetrofitClient.getInstance(sessionManager).getApiService()
                .crearCita(request)
                .enqueue(new Callback<CitaMedicaResponse>() {
                    @Override
                    public void onResponse(Call<CitaMedicaResponse> call,
                                           Response<CitaMedicaResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(),
                                    "Cita creada", Toast.LENGTH_SHORT).show();
                            cargarCitas();
                        }
                    }
                    @Override
                    public void onFailure(Call<CitaMedicaResponse> call, Throwable t) {
                        Toast.makeText(requireContext(),
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}