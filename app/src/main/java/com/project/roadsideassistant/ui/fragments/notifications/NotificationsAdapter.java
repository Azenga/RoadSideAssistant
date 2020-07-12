package com.project.roadsideassistant.ui.fragments.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.roadsideassistant.R;
import com.project.roadsideassistant.data.models.Notification;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private List<Notification> notifications;

    public NotificationsAdapter(List<Notification> notifications) {

        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.single_notification,
                                parent,
                                false
                        ));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        holder.titleTv.setText(notification.getTitle());
        holder.bodyTv.setText(notification.getBody());
        holder.costTv.setText(String.format("KSHs %s", notification.getCost()));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv, bodyTv, costTv;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.notification_title_tv);
            bodyTv = itemView.findViewById(R.id.notification_body_tv);
            costTv = itemView.findViewById(R.id.notification_cost_tv);
        }
    }
}
