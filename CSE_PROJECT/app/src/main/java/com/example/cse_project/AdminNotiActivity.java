package com.example.cse_project;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class AdminNotiActivity extends AppCompatActivity {

    private RecyclerView notificationRecyclerView;
    private AddNotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private DatabaseReference notificationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_noti_activity);

        // Khởi tạo RecyclerView và danh sách thông báo
        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);
        notificationList = new ArrayList<>();
        notificationAdapter = new AddNotificationAdapter(this, notificationList, new AddNotificationAdapter.OnNotificationClickListener() {
            @Override
            public void onEditClick(Notification notification) {
                // Xử lý hành động chỉnh sửa thông báo
                Intent intent = new Intent(AdminNotiActivity.this, EditNotiActivity.class);
                intent.putExtra("NOTI_KEY", notification.getNotiKey());
                intent.putExtra("NOTIFICATION_NAME", notification.getNotificationName());
                intent.putExtra("NOTICE_DESCRIPTION", notification.getNoticeDescription());
                startActivity(intent);  // Khởi động EditNotiActivity
            }

            @Override
            public void onDeleteClick(Notification notification) {
                // Xử lý hành động xóa thông báo
                deleteNotification(notification);
            }
        });

        // Cài đặt RecyclerView
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationRecyclerView.setAdapter(notificationAdapter);

        // Khởi tạo Firebase Database
        notificationRef = FirebaseDatabase.getInstance().getReference("Notification");

        // Lấy dữ liệu từ Firebase
        loadNotifications();

        FloatingActionButton addNotificationButton = findViewById(R.id.addNotificationButton);
        addNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminNotiActivity.this, AddNotiActivity.class);
                startActivity(intent);
            }
        });


        ImageView productImageView = findViewById(R.id.product);
        productImageView.setOnClickListener(v -> {
            Intent intent = new Intent(AdminNotiActivity.this, ProductActivity.class);
            startActivity(intent);
        });

        ImageView revenueImageView = findViewById(R.id.revenue);
        revenueImageView.setOnClickListener(v -> {
            Intent intent = new Intent(AdminNotiActivity.this, AdminActivity.class);
            startActivity(intent);
        });
    }

    private void loadNotifications() {
        notificationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification notification = snapshot.getValue(Notification.class);
                    if (notification != null) {
                        notificationList.add(notification);
                    }
                }
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminNotiActivity.this, "Lỗi khi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteNotification(Notification notification) {
        notificationRef.child(notification.getNotiKey()).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminNotiActivity.this, "Xóa thông báo thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminNotiActivity.this, "Xóa thông báo thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
