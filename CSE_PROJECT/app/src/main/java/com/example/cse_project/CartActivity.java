package com.example.cse_project;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceTextView = findViewById(R.id.totalPrice);
        checkoutButton = findViewById(R.id.checkoutButton);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this, cartList);
        cartRecyclerView.setAdapter(cartAdapter);

        cartReference = FirebaseDatabase.getInstance().getReference("Cart").child("UserCart");

        loadCartData();

        checkoutButton.setOnClickListener(v -> {
            if (!cartList.isEmpty()) {
                Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                // Chuyển dữ liệu sang bảng Order, xóa giỏ hàng, v.v.
            } else {
                Toast.makeText(this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView userIcon = findViewById(R.id.useractivity);
        userIcon.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, UserActivity.class)));

        ImageView homeIcon = findViewById(R.id.mainactivity);
        homeIcon.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, MainActivity.class)));

        ImageView categoryIcon = findViewById(R.id.categoryactivity);
        categoryIcon.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, CategoryActivity.class)));
    }

    private void loadCartData() {
        cartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                int totalPrice = 0;

                for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                    Cart cartItem = cartSnapshot.getValue(Cart.class);
                    if (cartItem != null) {
                        cartList.add(cartItem);
                        totalPrice += cartItem.getPrice() * cartItem.getQuantity();
                    }
                }

                totalPriceTextView.setText("Tổng: " + totalPrice + " VND");
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Lỗi khi tải giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
