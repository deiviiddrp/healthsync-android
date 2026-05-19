package com.healthsync.android.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.healthsync.android.adapter.ParametroVitalAdapter;
import com.healthsync.android.databinding.FragmentHistorialBinding;
import com.healthsync.android.model.ParametroVitalRequest;
import com.healthsync.android.model.ParametroVitalResponse;
import com.healthsync.android.repository.ParametroVitalRepository;
import com.healthsync.android.util.SessionManager;
import com.healthsync.android.viewmodel.ParametroVitalViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistorialFragment extends Fragment {

    private FragmentHistorialBinding binding;
    private ParametroVitalViewModel viewModel;
    private ParametroVitalAdapter adapter;
    private String filtroActual = "TODOS";

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
        setupFiltros();
        setupFab();
        setupGrafico();
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

    private void setupFiltros() {
        binding.chipTodos.setOnClickListener(v -> {
            filtroActual = "TODOS";
            binding.lineChart.setVisibility(View.GONE);
            actualizarLista(viewModel.getMediciones().getValue());
        });
        binding.chipTension.setOnClickListener(v -> {
            filtroActual = "TENSION_SISTOLICA";
            actualizarLista(viewModel.getMediciones().getValue());
            actualizarGrafico(viewModel.getMediciones().getValue(), "TENSION_SISTOLICA");
        });
        binding.chipGlucemia.setOnClickListener(v -> {
            filtroActual = "GLUCEMIA";
            actualizarLista(viewModel.getMediciones().getValue());
            actualizarGrafico(viewModel.getMediciones().getValue(), "GLUCEMIA");
        });
        binding.chipPeso.setOnClickListener(v -> {
            filtroActual = "PESO";
            actualizarLista(viewModel.getMediciones().getValue());
            actualizarGrafico(viewModel.getMediciones().getValue(), "PESO");
        });
        binding.chipFC.setOnClickListener(v -> {
            filtroActual = "FC";
            actualizarLista(viewModel.getMediciones().getValue());
            actualizarGrafico(viewModel.getMediciones().getValue(), "FC");
        });
    }

    private void setupGrafico() {
        LineChart chart = binding.lineChart;
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setDrawGridBackground(false);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setGranularity(1f);
        chart.getAxisLeft().setTextColor(Color.parseColor("#555555"));
        chart.getXAxis().setTextColor(Color.parseColor("#555555"));
        chart.getLegend().setTextColor(Color.parseColor("#1A1A1A"));
    }

    private void actualizarGrafico(List<ParametroVitalResponse> mediciones,
                                   String tipo) {
        if (mediciones == null || mediciones.isEmpty()) return;

        List<ParametroVitalResponse> filtradas = mediciones.stream()
                .filter(m -> m.getTipoParametro().equals(tipo))
                .collect(Collectors.toList());

        if (filtradas.isEmpty()) {
            binding.lineChart.setVisibility(View.GONE);
            return;
        }

        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = filtradas.size() - 1; i >= 0; i--) {
            ParametroVitalResponse m = filtradas.get(i);
            entries.add(new Entry(filtradas.size() - 1 - i,
                    (float) m.getValor()));
            String fecha = m.getFechaHora() != null
                    ? formatFechaCorta(m.getFechaHora()) : "";
            labels.add(fecha);
        }

        LineDataSet dataSet = new LineDataSet(entries, formatTipo(tipo));
        dataSet.setColor(Color.parseColor("#1976D2"));
        dataSet.setCircleColor(Color.parseColor("#1976D2"));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawValues(true);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.parseColor("#1A1A1A"));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#E3F2FD"));
        dataSet.setFillAlpha(100);

        binding.lineChart.getXAxis().setValueFormatter(
                new IndexAxisValueFormatter(labels));
        binding.lineChart.getXAxis().setLabelCount(
                Math.min(labels.size(), 5));

        binding.lineChart.setData(new LineData(dataSet));
        binding.lineChart.setVisibility(View.VISIBLE);
        binding.lineChart.animateX(800);
        binding.lineChart.invalidate();
    }

    private void actualizarLista(List<ParametroVitalResponse> mediciones) {
        if (mediciones == null) return;
        if (filtroActual.equals("TODOS")) {
            adapter.submitList(new ArrayList<>(mediciones));
        } else {
            String filtro = filtroActual;
            adapter.submitList(mediciones.stream()
                    .filter(m -> m.getTipoParametro().equals(filtro))
                    .collect(Collectors.toList()));
        }
    }

    private void setupFab() {
        binding.fabRegistrar.setOnClickListener(v -> {
            String[] tipos = {"TENSION_SISTOLICA", "GLUCEMIA",
                    "PESO", "FC", "SPO2", "TEMPERATURA"};
            String[] etiquetas = {"Tensión sistólica", "Glucemia",
                    "Peso", "Frec. cardíaca", "Saturación O₂", "Temperatura"};
            String[] unidades = {"mmHg", "mg/dL", "kg", "bpm", "%", "°C"};

            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Tipo de parámetro")
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
                                    String valorStr = input.getText().toString();
                                    if (!valorStr.isEmpty()) {
                                        double valor = Double.parseDouble(valorStr);
                                        viewModel.registrarMedicion(
                                                new ParametroVitalRequest(
                                                        tipos[which], valor,
                                                        unidades[which], ""));
                                    }
                                })
                                .setNegativeButton("Cancelar", null)
                                .show();
                    }).show();
        });
    }

    private void observeData() {
        viewModel.getMediciones().observe(getViewLifecycleOwner(),
                mediciones -> {
                    actualizarLista(mediciones);
                    if (!filtroActual.equals("TODOS")) {
                        actualizarGrafico(mediciones, filtroActual);
                    }
                });

        viewModel.getLoading().observe(getViewLifecycleOwner(),
                isLoading -> binding.progressBar.setVisibility(
                        isLoading ? View.VISIBLE : View.GONE));

        viewModel.getError().observe(getViewLifecycleOwner(),
                msg -> Toast.makeText(requireContext(),
                        msg, Toast.LENGTH_SHORT).show());
    }

    private String formatFechaCorta(String fechaHora) {
        try {
            String[] parts = fechaHora.split("T");
            String[] fecha = parts[0].split("-");
            return fecha[2] + "/" + fecha[1];
        } catch (Exception e) {
            return fechaHora;
        }
    }

    private String formatTipo(String tipo) {
        return switch (tipo) {
            case "TENSION_SISTOLICA"  -> "Tensión sistólica";
            case "TENSION_DIASTOLICA" -> "Tensión diastólica";
            case "GLUCEMIA"    -> "Glucemia";
            case "PESO"        -> "Peso";
            case "FC"          -> "Frec. cardíaca";
            case "SPO2"        -> "Saturación O₂";
            case "TEMPERATURA" -> "Temperatura";
            default -> tipo;
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}