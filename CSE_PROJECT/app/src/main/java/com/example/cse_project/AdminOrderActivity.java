package com.example.cse_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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

public class AdminOrderActivity extends AppCompatActivity {

    private RecyclerView orderRecyclerView;
    private AdminOrderAdapter orderAdapter;
    private FirebaseDatabase database;
    private DatabaseReference ordersRef;
    private List<AdminOrder> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_order_activity);

        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        ordersRef = database.getReference("orders");

        loadOrders();

        setupNavigation();
    }

    private void loadOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    AdminOrder order = orderSnapshot.getValue(AdminOrder.class);
                    if (order != null) {
                        orderList.add(order);
                    }
                }

                orderAdapter = new AdminOrderAdapter(AdminOrderActivity.this, orderList, new AdminOrderAdapter.OnOrderClickListener() {
                    @Override
                    public void onShipButtonClick(AdminOrder order) {
                        if ("Đang xử lý".equals(order.getStatus())) {
                            updateOrderStatus(order.getOrderId(), "Đang được giao hàng");
                        } else {
                            Toast.makeText(AdminOrderActivity.this, "Không thể giao hàng vì đơn hàng không ở trạng thái 'Đang xử lý'.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onConfirmButtonClick(AdminOrder order) {
                        if ("Đang được giao hàng".equals(order.getStatus())) {
                            updateOrderStatus(order.getOrderId(), "Đã tới nơi");
                        } else {
                            Toast.makeText(AdminOrderActivity.this, "Không thể xác nhận vì đơn hàng không ở trạng thái 'Đang được giao hàng'.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                orderRecyclerView.setAdapter(orderAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminOrderActivity.this, "Lỗi khi tải đơn hàng.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOrderStatus(String orderId, String newStatus) {
        DatabaseReference orderRef = ordersRef.child(orderId);
        orderRef.child("status").setValue(newStatus).addOnSuccessListener(aVoid ->
                Toast.makeText(AdminOrderActivity.this, "Cập nhật trạng thái thành công.", Toast.LENGTH_SHORT).show()
        ).addOnFailureListener(e ->
                Toast.makeText(AdminOrderActivity.this, "Cập nhật trạng thái thất bại.", Toast.LENGTH_SHORT).show()
        );
    }

    private void setupNavigation() {
        findViewById(R.id.doanhthu).setOnClickListener(view -> navigateToAdminActivity());
        findViewById(R.id.sanpham).setOnClickListener(view -> navigateToProductActivity());
        findViewById(R.id.thongbao).setOnClickListener(view -> navigateToAdminNotiActivity());
    }

    private void navigateToAdminActivity() {
        Intent intent = new Intent(AdminOrderActivity.this, AdminActivity.class);
        startActivity(intent);
    }


    private void navigateToProductActivity() {
        Intent intent = new Intent(AdminOrderActivity.this, ProductActivity.class);
        startActivity(intent);
    }


    private void navigateToAdminNotiActivity() {
        Intent intent = new Intent(AdminOrderActivity.this, AdminNotiActivity.class);
        startActivity(intent);
    }
}
