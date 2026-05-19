package com.healthsync.android.adapter;

import android.graphics.Color;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.healthsync.android.databinding.ItemParametroVitalBinding;
import com.healthsync.android.model.ParametroVitalResponse;

public class ParametroVitalAdapter extends
        ListAdapter<ParametroVitalResponse, ParametroVitalAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<ParametroVitalResponse> DIFF =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull ParametroVitalResponse a,
                                               @NonNull ParametroVitalResponse b) {
                    return a.getId() == b.getId();
                }
                @Override
                public boolean areContentsTheSame(@NonNull ParametroVitalResponse a,
                                                  @NonNull ParametroVitalResponse b) {
                    return a.equals(b);
                }
            };

    public ParametroVitalAdapter() { super(DIFF); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemParametroVitalBinding binding = ItemParametroVitalBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemParametroVitalBinding b;

        ViewHolder(ItemParametroVitalBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(ParametroVitalResponse item) {
            b.tvTipo.setText(formatTipo(item.getTipoParametro()));
            b.tvValor.setText(item.getValor() + " " + item.getUnidad());
            b.tvFecha.setText(formatFecha(item.getFechaHora()));

            String semaforo = item.getEstadoSemaforo();
            int color;
            if (semaforo == null) {
                color = Color.parseColor("#388E3C");
            } else {
                color = switch (semaforo) {
                    case "CRITICO" -> Color.parseColor("#D32F2F");
                    case "LIMITE"  -> Color.parseColor("#FFA000");
                    default        -> Color.parseColor("#388E3C");
                };
            }
            b.viewSemaforo.setBackgroundColor(color);
        }

        private String formatFecha(String fechaHora) {
            if (fechaHora == null) return "";
            try {
                String[] parts = fechaHora.split("T");
                String[] fecha = parts[0].split("-");
                String hora = parts[1].substring(0, 5);
                return fecha[2] + "/" + fecha[1] + "/" + fecha[0] + " " + hora;
            } catch (Exception e) {
                return fechaHora;
            }
        }

        private String formatTipo(String tipo) {
            if (tipo == null) return "";
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
    }
}