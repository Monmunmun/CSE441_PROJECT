package com.example.cse_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.OrderViewHolder> {

    private List<AdminOrder> orderList;
    private Context context;
    private OnOrderClickListener onOrderClickListener;

    public AdminOrderAdapter(Context context, List<AdminOrder> orderList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.orderList = orderList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        AdminOrder order = orderList.get(position);
        holder.orderIdTextView.setText("Mã đơn hàng: " + order.getOrderId());
        holder.userIdTextView.setText("Tên khách hàng: " + order.getUserId());
        holder.totalAmountTextView.setText("Tổng tiền: " + order.getTotalAmount() + " VND");
        holder.orderDateTextView.setText("Ngày đặt hàng: " + order.getOrderDate());
        holder.statusTextView.setText("Trạng thái: " + order.getStatus());
        holder.shippingAddressTextView.setText("Nơi giao hàng: " + order.getShippingAddress());

        // Thiết lập sự kiện cho các nút
        holder.shipButton.setOnClickListener(v -> onOrderClickListener.onShipButtonClick(order));
        holder.confirmButton.setOnClickListener(v -> onOrderClickListener.onConfirmButtonClick(order));
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView userIdTextView;
        TextView totalAmountTextView;
        TextView orderDateTextView;
        TextView statusTextView;
        TextView shippingAddressTextView;
        ImageButton shipButton;
        ImageButton confirmButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.order_id);
            userIdTextView = itemView.findViewById(R.id.user_id);
            totalAmountTextView = itemView.findViewById(R.id.total_amount);
            orderDateTextView = itemView.findViewById(R.id.order_date);
            statusTextView = itemView.findViewById(R.id.status);
            shippingAddressTextView = itemView.findViewById(R.id.shipping_address); // Thêm dòng này
            shipButton = itemView.findViewById(R.id.ship_button);
            confirmButton = itemView.findViewById(R.id.confirm_button);
        }
    }


    public interface OnOrderClickListener {
        void onShipButtonClick(AdminOrder order);
        void onConfirmButtonClick(AdminOrder order);
    }
}
