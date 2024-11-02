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
        setContentView(R.layout.add_noti_activity);


        notificationNameEditText = findViewById(R.id.notificationNameEditText);
        noticeDescriptionEditText = findViewById(R.id.noticeDescriptionEditText);
        addNotificationButton = findViewById(R.id.addNotificationButton);


        notificationRef = FirebaseDatabase.getInstance().getReference("Notification");


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


        String notiKey = notificationRef.push().getKey();
        Notification notification = new Notification(notiKey, notificationName, noticeDescription);


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
