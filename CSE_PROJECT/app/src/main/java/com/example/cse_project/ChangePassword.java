package com.example.cse_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {
    private EditText currentPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private Button changePasswordButton;
    private DatabaseReference userReference;
    private String username;
    private ImageView backButton;// To store the logged-in user's username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        currentPasswordEditText = findViewById(R.id.edtOldPass);
        newPasswordEditText = findViewById(R.id.edtNewPass);
        confirmPasswordEditText = findViewById(R.id.edtNewPass2);
        changePasswordButton = findViewById(R.id.changePassButton);
        backButton = findViewById(R.id.back_button);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        userReference = FirebaseDatabase.getInstance().getReference("User");

        changePasswordButton.setOnClickListener(v -> changeUserPassword());

        backButton.setOnClickListener(v -> finish());
    }

    private void changeUserPassword() {
        String currentPassword = currentPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        userReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Users user = userSnapshot.getValue(Users.class);
                        if (user != null && user.getPassword().equals(currentPassword)) {
                            userSnapshot.getRef().child("password").setValue(newPassword);
                            Toast.makeText(ChangePassword.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ChangePassword.this, UserActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(ChangePassword.this, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(ChangePassword.this, "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChangePassword.this, "Thay đổi mật khẩu thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}