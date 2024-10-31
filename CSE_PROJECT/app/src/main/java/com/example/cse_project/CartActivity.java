package com.example.cse_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private TextView totalPriceTextView;
    private Button checkoutButton;
    private CartAdapter cartAdapter;
    private List<Cart> cartList = new ArrayList<>();
    private DatabaseReference cartReference;
    private double totalAmount = 0.0;

    private ImageView mainActivityImageView;
    private ImageView categoryActivityImageView;
    private ImageView userActivityImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);


        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceTextView = findViewById(R.id.totalPrice);
        checkoutButton = findViewById(R.id.checkoutButton);

        mainActivityImageView = findViewById(R.id.mainactivity);
        categoryActivityImageView = findViewById(R.id.categoryactivity);
        userActivityImageView = findViewById(R.id.useractivity);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this, cartList, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged(Cart cartItem) {
                updateCartItem(cartItem);
            }

            @Override
            public void onItemRemoved(Cart cartItem) {
                removeCartItem(cartItem);
            }
        });
        cartRecyclerView.setAdapter(cartAdapter);


        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "default_username");


        cartReference = FirebaseDatabase.getInstance().getReference("carts").child(username).child("items");


        loadCartItems();


        checkoutButton.setOnClickListener(v -> {
            Toast.makeText(CartActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();

        });

        mainActivityImageView.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        categoryActivityImageView.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CategoryActivity.class);
            startActivity(intent);
            finish();
        });

        userActivityImageView.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, UserActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadCartItems() {
        cartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                totalAmount = 0.0;
                for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                    Cart cartItem = cartSnapshot.getValue(Cart.class);
                    cartList.add(cartItem);
                    totalAmount += cartItem.getTotalPrice();
                }
                cartAdapter.notifyDataSetChanged();
                totalPriceTextView.setText("Tổng: " + totalAmount + " VND");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Lỗi khi tải giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCartItem(Cart cartItem) {
        cartReference.child(cartItem.getBookId()).setValue(cartItem).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CartActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CartActivity.this, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeCartItem(Cart cartItem) {
        cartReference.child(cartItem.getBookId()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CartActivity.this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CartActivity.this, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
