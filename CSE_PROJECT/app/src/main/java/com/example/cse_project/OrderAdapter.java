package com.example.cse_project;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderIdTextView.setText("Mã đơn hàng: " + order.getOrderId());
        holder.orderDateTextView.setText("Ngày đặt hàng: " + order.getOrderDate());
        holder.totalAmountTextView.setText("Tổng tiền: " + order.getTotalAmount());
        holder.statusTextView.setText("Trạng thái: " + order.getStatus());

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), OrderItemActivity.class);


            intent.putExtra("orderId", order.getOrderId());
            intent.putExtra("totalAmount", order.getTotalAmount());
            intent.putExtra("orderDate", order.getOrderDate());
            intent.putExtra("status", order.getStatus());


            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView orderDateTextView;
        TextView totalAmountTextView;
        TextView statusTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            totalAmountTextView = itemView.findViewById(R.id.totalAmountTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }
}

