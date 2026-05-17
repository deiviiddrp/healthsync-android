package com.healthsync.android.adapter;

import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.healthsync.android.databinding.ItemAlertaBinding;
import com.healthsync.android.model.AlertaResponse;

public class AlertaAdapter extends
        ListAdapter<AlertaResponse, AlertaAdapter.ViewHolder> {

    public interface OnAlertaListener {
        void onToggle(AlertaResponse alerta, boolean activa);
        void onDelete(AlertaResponse alerta);
    }

    private final OnAlertaListener listener;

    private static final DiffUtil.ItemCallback<AlertaResponse> DIFF =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull AlertaResponse a,
                                               @NonNull AlertaResponse b) {
                    return a.getId() == b.getId();
                }
                @Override
                public boolean areContentsTheSame(@NonNull AlertaResponse a,
                                                  @NonNull AlertaResponse b) {
                    return a.isActiva() == b.isActiva();
                }
            };

    public AlertaAdapter(OnAlertaListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlertaBinding binding = ItemAlertaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAlertaBinding b;

        ViewHolder(ItemAlertaBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(AlertaResponse item) {
            b.tvTipoParametro.setText(formatTipo(item.getTipoParametro()));
            b.tvCondicion.setText("Notificar si " +
                    item.getOperador() + " " +
                    item.getValorUmbral());
            b.switchActiva.setChecked(item.isActiva());
            b.switchActiva.setOnCheckedChangeListener((btn, checked) ->
                    listener.onToggle(item, checked));
            b.getRoot().setOnLongClickListener(v -> {
                listener.onDelete(item);
                return true;
            });
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