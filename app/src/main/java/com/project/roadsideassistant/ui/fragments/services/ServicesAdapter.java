package com.project.roadsideassistant.ui.fragments.services;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.roadsideassistant.R;
import com.project.roadsideassistant.data.models.Service;

import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {

    private List<Service> services;

    public ServicesAdapter(List<Service> services) {
        this.services = services;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        holder.nameTV.setText(services.get(position).getName());
        holder.descriptionTV.setText(services.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, descriptionTV;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.name);
            descriptionTV = itemView.findViewById(R.id.description);
        }
    }
}
