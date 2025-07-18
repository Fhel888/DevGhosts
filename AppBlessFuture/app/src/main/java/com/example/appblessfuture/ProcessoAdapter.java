package com.example.appblessfuture;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProcessoAdapter extends RecyclerView.Adapter<ProcessoAdapter.ViewHolder> {

    public interface OnProcessoAction {
        void visualizar(Processo p);
        void excluir(Processo p);
    }

    private final List<Processo> lista;
    private final OnProcessoAction listener;

    public ProcessoAdapter(List<Processo> lista, OnProcessoAction listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtItemProcessoNome, txtItemProcessoSetor, txtItemProcessoData;
        Button btItemProcessoPdf, btItemProcessoExcluir;

        public ViewHolder(View view) {
            super(view);
            txtItemProcessoNome = view.findViewById(R.id.txtItemProcessoNome);
            txtItemProcessoSetor = view.findViewById(R.id.txtItemProcessoSetor);
            txtItemProcessoData = view.findViewById(R.id.txtItemProcessoData);
            btItemProcessoPdf = view.findViewById(R.id.btItemProcessoPdf);
            btItemProcessoExcluir = view.findViewById(R.id.btItemProcessoExcluir);
        }
    }

    @NonNull
    @Override
    public ProcessoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_processo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessoAdapter.ViewHolder holder, int position) {
        Processo p = lista.get(position);
        holder.txtItemProcessoNome.setText("Nome: " + p.getNome());
        holder.txtItemProcessoSetor.setText("Setor: " + p.getSetor());
        holder.txtItemProcessoData.setText("Data: " + p.getData());


        holder.btItemProcessoPdf.setOnClickListener(v -> listener.visualizar(p));
        holder.btItemProcessoExcluir.setOnClickListener(v -> listener.excluir(p));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
