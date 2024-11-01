package com.example.cse_project;

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
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private EditText usernameEditText, nameEditText, emailEditText, phoneNumberEditText, addressEditText;
    private ImageView avatarImageView, backButton;
    private Button saveButton;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);


        avatarImageView = findViewById(R.id.avatar);
        usernameEditText = findViewById(R.id.username);
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        addressEditText = findViewById(R.id.address);
        backButton = findViewById(R.id.back_button);
        saveButton = findViewById(R.id.save_button);


        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        userReference = FirebaseDatabase.getInstance().getReference("User");


        loadUserData(username);


        backButton.setOnClickListener(v -> finish());


        saveButton.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData(String username) {
        userReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Users user = userSnapshot.getValue(Users.class);
                        if (user != null) {
                            usernameEditText.setText(user.getUsername());
                            nameEditText.setText(user.getName());
                            emailEditText.setText(user.getEmail());
                            phoneNumberEditText.setText(user.getPhoneNumber());
                            addressEditText.setText(user.getAddress());

                            Picasso.get().load(user.getAvatar()).into(avatarImageView);
                        }
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Lỗi khi tải dữ liệu người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String address = addressEditText.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = usernameEditText.getText().toString();
        userReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        userSnapshot.getRef().child("name").setValue(name);
                        userSnapshot.getRef().child("email").setValue(email);
                        userSnapshot.getRef().child("phoneNumber").setValue(phoneNumber);
                        userSnapshot.getRef().child("address").setValue(address);

                        Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
