package com.example.cse_project;

import android.content.Intent;
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

public class EditNotiActivity extends AppCompatActivity {

    private EditText notificationNameEditText;
    private EditText noticeDescriptionEditText;
    private Button saveNotificationButton;
    private DatabaseReference notificationRef;
    private String notiKey; // Khóa thông báo để cập nhật

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_noti_activity);

        // Khởi tạo các View
        notificationNameEditText = findViewById(R.id.notificationNameEditText);
        noticeDescriptionEditText = findViewById(R.id.noticeDescriptionEditText);
        saveNotificationButton = findViewById(R.id.SaveNotificationButton);
        ImageView backButton = findViewById(R.id.back_button);

        // Khởi tạo Firebase Database
        notificationRef = FirebaseDatabase.getInstance().getReference("Notification");

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            notiKey = intent.getStringExtra("NOTI_KEY"); // Nhận khóa thông báo
            String notificationName = intent.getStringExtra("NOTIFICATION_NAME");
            String noticeDescription = intent.getStringExtra("NOTICE_DESCRIPTION");

            // Hiển thị dữ liệu trong các EditText
            notificationNameEditText.setText(notificationName);
            noticeDescriptionEditText.setText(noticeDescription);
        }

        saveNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotification();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại Activity trước đó
            }
        });
    }

    private void saveNotification() {
        String notificationName = notificationNameEditText.getText().toString().trim();
        String noticeDescription = noticeDescriptionEditText.getText().toString().trim();

        if (notificationName.isEmpty() || noticeDescription.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }


        Notification updatedNotification = new Notification(notiKey, notificationName, noticeDescription);
        notificationRef.child(notiKey).setValue(updatedNotification)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditNotiActivity.this, "Cập nhật thông báo thành công", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại AdminNotiActivity
                    } else {
                        Toast.makeText(EditNotiActivity.this, "Cập nhật thông báo thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
