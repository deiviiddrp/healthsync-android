package com.healthsync.android.ui;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.healthsync.android.adapter.ParametroVitalAdapter;
import com.healthsync.android.databinding.FragmentHistorialBinding;
import com.healthsync.android.repository.ParametroVitalRepository;
import com.healthsync.android.util.SessionManager;
import com.healthsync.android.viewmodel.ParametroVitalViewModel;

public class HistorialFragment extends Fragment {

    private FragmentHistorialBinding binding;
    private ParametroVitalViewModel viewModel;
    private ParametroVitalAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistorialBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupRecyclerView();
        setupFab();
        observeData();
        viewModel.cargarHistorial();
    }

    private void setupViewModel() {
        SessionManager session = new SessionManager(requireContext());
        ParametroVitalRepository repo = new ParametroVitalRepository(
                session, requireContext());
        ParametroVitalViewModel.Factory factory =
                new ParametroVitalViewModel.Factory(repo);
        viewModel = new ViewModelProvider(this, factory)
                .get(ParametroVitalViewModel.class);
    }

    private void setupRecyclerView() {
        adapter = new ParametroVitalAdapter();
        binding.rvHistorial.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        binding.rvHistorial.setAdapter(adapter);
    }

    private void setupFab() {
        binding.fabRegistrar.setOnClickListener(v -> {
            String[] tipos = {"TENSION_SISTOLICA", "GLUCEMIA", "PESO", "FC", "SPO2"};
            String[] unidades = {"mmHg", "mg/dL", "kg", "bpm", "%"};

            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Tipo de parámetro")
                    .setItems(tipos, (dialog, which) -> {
                        android.widget.EditText input = new android.widget.EditText(requireContext());
                        input.setHint("Valor");
                        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER
                                | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                .setTitle("Introduce el valor")
                                .setView(input)
                                .setPositiveButton("Guardar", (d, w) -> {
                                    String valorStr = input.getText().toString();
                                    if (!valorStr.isEmpty()) {
                                        double valor = Double.parseDouble(valorStr);
                                        viewModel.registrarMedicion(
                                                new com.healthsync.android.model.ParametroVitalRequest(
                                                        tipos[which], valor, unidades[which], ""));
                                    }
                                })
                                .setNegativeButton("Cancelar", null)
                                .show();
                    })
                    .show();
        });
    }

    private void observeData() {
        viewModel.getMediciones().observe(getViewLifecycleOwner(),
                mediciones -> adapter.submitList(mediciones));

        viewModel.getLoading().observe(getViewLifecycleOwner(),
                isLoading -> binding.progressBar.setVisibility(
                        isLoading ? View.VISIBLE : View.GONE));

        viewModel.getError().observe(getViewLifecycleOwner(),
                msg -> Toast.makeText(requireContext(),
                        msg, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}