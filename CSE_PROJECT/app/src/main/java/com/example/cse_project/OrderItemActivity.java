package com.example.cse_project;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

public class OrderItemActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrderDetails;
    private OrderItemAdapter orderItemAdapter;
    private List<OrderItem> orderItemList = new ArrayList<>();
    private TextView totalAmountTextView;
    private DatabaseReference ordersReference;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_activity);


        orderId = getIntent().getStringExtra("orderId");

        // Khởi tạo RecyclerView
        recyclerViewOrderDetails = findViewById(R.id.recyclerViewOrderDetails);
        recyclerViewOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        orderItemAdapter = new OrderItemAdapter(this, orderItemList);
        recyclerViewOrderDetails.setAdapter(orderItemAdapter);

        // Khởi tạo TextView để hiển thị tổng số tiền
        totalAmountTextView = findViewById(R.id.totalAmountTextView);


        ordersReference = FirebaseDatabase.getInstance().getReference("orders").child(orderId);
        loadOrderDetails();


        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void loadOrderDetails() {
        ordersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    double totalAmount = snapshot.child("totalAmount").getValue(Double.class);
                    totalAmountTextView.setText("Tổng số tiền: " + totalAmount + " VND");


                    DataSnapshot itemsSnapshot = snapshot.child("items");
                    for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
                        OrderItem orderItem = itemSnapshot.getValue(OrderItem.class);
                        if (orderItem != null) {
                            orderItemList.add(orderItem);
                        }
                    }

                    orderItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

