package com.example.cse_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNotiActivity extends AppCompatActivity {

    private EditText notificationNameEditText;
    private EditText noticeDescriptionEditText;
    private Button addNotificationButton;
    private DatabaseReference notificationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_noti_activity); // Đảm bảo tên layout khớp với tệp XML bạn đã tạo

        // Khởi tạo các view
        notificationNameEditText = findViewById(R.id.notificationNameEditText);
        noticeDescriptionEditText = findViewById(R.id.noticeDescriptionEditText);
        addNotificationButton = findViewById(R.id.addNotificationButton);

        // Khởi tạo Firebase Database
        notificationRef = FirebaseDatabase.getInstance().getReference("Notification");

        // Xử lý sự kiện nhấn nút Thêm Thông Báo
        addNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotification();
            }
        });

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void addNotification() {
        String notificationName = notificationNameEditText.getText().toString().trim();
        String noticeDescription = noticeDescriptionEditText.getText().toString().trim();


        if (notificationName.isEmpty() || noticeDescription.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo một đối tượng Notification mới
        String notiKey = notificationRef.push().getKey();
        Notification notification = new Notification(notiKey, notificationName, noticeDescription);

        // Lưu thông báo vào Firebase
        notificationRef.child(notiKey).setValue(notification).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddNotiActivity.this, "Thêm thông báo thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddNotiActivity.this, "Thêm thông báo thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
