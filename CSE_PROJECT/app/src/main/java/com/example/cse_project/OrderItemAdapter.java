package com.example.cse_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private List<OrderItem> orderItemList;
    private Context context;

    public OrderItemAdapter(Context context, List<OrderItem> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_detail_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);
        holder.bind(orderItem);
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private TextView bookTitle;
        private TextView price;
        private TextView quantity;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
        }

        public void bind(OrderItem orderItem) {
            bookTitle.setText(orderItem.getBookTitle());
            price.setText("Giá: " + orderItem.getPrice() + " VND");
            quantity.setText("Số lượng: " + orderItem.getQuantity());
        }
    }
}
