package com.project.roadsideassistant.ui.fragments.gethelp.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.roadsideassistant.R;
import com.project.roadsideassistant.data.models.Service;

import java.util.List;

public class ChooseServiceAdapter extends RecyclerView.Adapter<ChooseServiceAdapter.ServiceViewHolder> {

    private List<Service> services;

    public ChooseServiceAdapter(List<Service> services) {
        this.services = services;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_select_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        holder.checkbox.setText(services.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkbox;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }
}
