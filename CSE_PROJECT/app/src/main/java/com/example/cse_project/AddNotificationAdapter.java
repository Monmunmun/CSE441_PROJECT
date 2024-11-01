package com.example.cse_project;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddNotificationAdapter extends RecyclerView.Adapter<AddNotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;
    private Context context;
    private OnNotificationClickListener listener;

    public AddNotificationAdapter(Context context, List<Notification> notificationList, OnNotificationClickListener listener) {
        this.context = context;
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_noti_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.notificationNameText.setText(notification.getNotificationName());
        holder.notificationText.setText(notification.getNoticeDescription());

        // Xử lý sự kiện cho nút sửa
        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(notification);  // Gọi phương thức từ listener
            }
        });

        // Xử lý sự kiện cho nút xóa
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(notification);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView notificationNameText;
        TextView notificationText;
        ImageButton editButton;
        ImageButton deleteButton;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationNameText = itemView.findViewById(R.id.notificationNameText);
            notificationText = itemView.findViewById(R.id.notificationText);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnNotificationClickListener {
        void onEditClick(Notification notification);
        void onDeleteClick(Notification notification);
    }
}

