package com.healthsync.android.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.healthsync.android.LoginActivity;
import com.healthsync.android.R;
import com.healthsync.android.databinding.FragmentDashboardBinding;
import com.healthsync.android.model.ParametroVitalResponse;
import com.healthsync.android.repository.ParametroVitalRepository;
import com.healthsync.android.util.SessionManager;
import com.healthsync.android.viewmodel.ParametroVitalViewModel;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private ParametroVitalViewModel viewModel;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        setupViewModel();
        setupHeader();
        setupButtons();
        viewModel.cargarHistorial();
        viewModel.getMediciones().observe(getViewLifecycleOwner(),
                this::actualizarTarjetas);
    }

    private void setupViewModel() {
        ParametroVitalRepository repo = new ParametroVitalRepository(
                sessionManager, requireContext());
        ParametroVitalViewModel.Factory factory =
                new ParametroVitalViewModel.Factory(repo);
        viewModel = new ViewModelProvider(this, factory)
                .get(ParametroVitalViewModel.class);
    }

    private void setupHeader() {
        int hora = java.util.Calendar.getInstance()
                .get(java.util.Calendar.HOUR_OF_DAY);
        String saludo;
        if (hora < 12) saludo = "Buenos días";
        else if (hora < 20) saludo = "Buenas tardes";
        else saludo = "Buenas noches";

        String email = sessionManager.getEmail();
        if (email != null) {
            String nombre = email.split("@")[0];
            saludo += ", " + nombre;
        }
        binding.tvSaludo.setText(saludo);

        java.time.LocalDate hoy = java.time.LocalDate.now();
        binding.tvFecha.setText(hoy.getDayOfMonth() + "/" +
                hoy.getMonthValue() + "/" + hoy.getYear());
    }

    private void setupButtons() {
        binding.btnCerrarSesion.setOnClickListener(v -> {
            sessionManager.clearSession();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });

        binding.btnVerHistorial.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.historialFragment));

        binding.btnRegistrarMedicion.setOnClickListener(v -> {
            String[] tipos = {"TENSION_SISTOLICA", "GLUCEMIA",
                    "PESO", "FC", "SPO2", "TEMPERATURA"};
            String[] etiquetas = {"Tensión sistólica", "Glucemia",
                    "Peso", "Frec. cardíaca", "Saturación O₂", "Temperatura"};
            String[] unidades = {"mmHg", "mg/dL", "kg", "bpm", "%", "°C"};

            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("¿Qué quieres registrar?")
                    .setItems(etiquetas, (dialog, which) -> {
                        android.widget.EditText input =
                                new android.widget.EditText(requireContext());
                        input.setHint("Valor en " + unidades[which]);
                        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER
                                | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                .setTitle(etiquetas[which])
                                .setView(input)
                                .setPositiveButton("Guardar", (d, w) -> {
                                    String val = input.getText().toString();
                                    if (!val.isEmpty()) {
                                        viewModel.registrarMedicion(
                                                new com.healthsync.android.model
                                                        .ParametroVitalRequest(
                                                        tipos[which],
                                                        Double.parseDouble(val),
                                                        unidades[which], ""));
                                    }
                                })
                                .setNegativeButton("Cancelar", null)
                                .show();
                    }).show();
        });
    }

    private void actualizarTarjetas(List<ParametroVitalResponse> mediciones) {
        if (mediciones == null) return;
        for (ParametroVitalResponse m : mediciones) {
            String semaforo = m.getEstadoSemaforo();
            int color = getColorSemaforo(semaforo);
            switch (m.getTipoParametro()) {
                case "TENSION_SISTOLICA":
                    binding.tvTension.setText(m.getValor() + " mmHg");
                    binding.viewSemaforoTension.setBackgroundColor(color);
                    break;
                case "GLUCEMIA":
                    binding.tvGlucemia.setText(m.getValor() + " mg/dL");
                    binding.viewSemaforoGlucemia.setBackgroundColor(color);
                    break;
                case "PESO":
                    binding.tvPeso.setText(m.getValor() + " kg");
                    binding.viewSemaforoPeso.setBackgroundColor(color);
                    break;
                case "FC":
                    binding.tvFC.setText(m.getValor() + " bpm");
                    binding.viewSemaforoFC.setBackgroundColor(color);
                    break;
            }
        }
    }

    private int getColorSemaforo(String semaforo) {
        if (semaforo == null) return Color.parseColor("#388E3C");
        return switch (semaforo) {
            case "CRITICO" -> Color.parseColor("#D32F2F");
            case "LIMITE"  -> Color.parseColor("#FFA000");
            default        -> Color.parseColor("#388E3C");
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}