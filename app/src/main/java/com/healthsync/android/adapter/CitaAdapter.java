package com.healthsync.android.adapter;

import android.graphics.Color;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.healthsync.android.databinding.ItemCitaBinding;
import com.healthsync.android.model.CitaMedicaResponse;

public class CitaAdapter extends
        ListAdapter<CitaMedicaResponse, CitaAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<CitaMedicaResponse> DIFF =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull CitaMedicaResponse a,
                                               @NonNull CitaMedicaResponse b) {
                    return a.getId() == b.getId();
                }
                @Override
                public boolean areContentsTheSame(@NonNull CitaMedicaResponse a,
                                                  @NonNull CitaMedicaResponse b) {
                    return a.getFechaHora().equals(b.getFechaHora());
                }
            };

    public CitaAdapter() { super(DIFF); }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCitaBinding binding = ItemCitaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCitaBinding b;

        ViewHolder(ItemCitaBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(CitaMedicaResponse item) {
            b.tvEspecialidad.setText(item.getEspecialidad());
            b.tvMedico.setText(item.getNombreMedico() != null
                    ? "Dr/a. " + item.getNombreMedico() : "");
            b.tvFecha.setText(formatFecha(item.getFechaHora()));
            b.tvEstado.setText(item.getEstado());

            int color = switch (item.getEstado()) {
                case "CONFIRMADA"  -> Color.parseColor("#388E3C");
                case "CANCELADA"   -> Color.parseColor("#D32F2F");
                case "COMPLETADA"  -> Color.parseColor("#757575");
                default            -> Color.parseColor("#1976D2");
            };
            b.viewEstado.setBackgroundColor(color);
            b.tvEstado.setBackgroundColor(color);
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
    }
}