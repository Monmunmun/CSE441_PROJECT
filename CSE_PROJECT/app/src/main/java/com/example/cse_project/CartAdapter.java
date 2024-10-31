package com.example.cse_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Cart> cartList;
    private CartListener cartListener; // Thêm listener


    public interface CartListener {
        void onQuantityChanged(Cart cartItem);
        void onItemRemoved(Cart cartItem);
    }

    public CartAdapter(Context context, List<Cart> cartList, CartListener cartListener) {
        this.context = context;
        this.cartList = cartList;
        this.cartListener = cartListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cartItem = cartList.get(position);
        holder.cartItemTitle.setText(cartItem.getBookTitle());
        holder.cartItemPrice.setText(cartItem.getPricePerItem() + " VND");
        holder.cartItemQuantity.setText("Số lượng: " + cartItem.getTotalQuantity());

        Picasso.get().load(cartItem.getBookImage()).into(holder.cartItemImage);


        holder.increaseButton.setOnClickListener(v -> {
            cartItem.setTotalQuantity(cartItem.getTotalQuantity() + 1);
            cartItem.setTotalPrice(cartItem.getPricePerItem() * cartItem.getTotalQuantity());
            cartListener.onQuantityChanged(cartItem);
            notifyItemChanged(position);
        });

        holder.decreaseButton.setOnClickListener(v -> {
            if (cartItem.getTotalQuantity() > 1) {
                cartItem.setTotalQuantity(cartItem.getTotalQuantity() - 1);
                cartItem.setTotalPrice(cartItem.getPricePerItem() * cartItem.getTotalQuantity());
                cartListener.onQuantityChanged(cartItem);
                notifyItemChanged(position);
            }
        });

        holder.removeButton.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa sản phẩm")
                    .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        cartList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartList.size());

                        if (cartListener != null) {
                            cartListener.onItemRemoved(cartItem);
                        }
                    })
                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView cartItemImage;
        TextView cartItemTitle, cartItemPrice, cartItemQuantity;
        ImageButton increaseButton, decreaseButton, removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemImage = itemView.findViewById(R.id.cartItemImage);
            cartItemTitle = itemView.findViewById(R.id.cartItemTitle);
            cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
            cartItemQuantity = itemView.findViewById(R.id.cartItemQuantity);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}

