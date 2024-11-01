package com.example.cse_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class UserActivity extends AppCompatActivity {
    private TextView usernameTextView, nameTextView, moneyTextView;
    private ImageView avatarImageView;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        usernameTextView = findViewById(R.id.username);
        nameTextView = findViewById(R.id.name);
        moneyTextView = findViewById(R.id.money);
        avatarImageView = findViewById(R.id.avatar);


        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        userReference = FirebaseDatabase.getInstance().getReference("User");

        loadUserData(username);

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> logout());

        Button accountInfoButton = findViewById(R.id.account_info_button);
        accountInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        ImageView homeIcon = findViewById(R.id.mainactivity);
        homeIcon.setOnClickListener(v -> startActivity(new Intent(UserActivity.this, MainActivity.class)));

        ImageView categoryIcon = findViewById(R.id.categoryactivity);
        categoryIcon.setOnClickListener(v -> startActivity(new Intent(UserActivity.this, CategoryActivity.class)));

        ImageView cartIcon = findViewById(R.id.cartactivity);
        cartIcon.setOnClickListener(v -> startActivity(new Intent(UserActivity.this, CartActivity.class)));

        ImageView helpIcon = findViewById(R.id.hotro);
        helpIcon.setOnClickListener(v -> startActivity(new Intent(UserActivity.this, HelpActivity.class)));

        Button notificationsButton = findViewById(R.id.notifications_button);
        notificationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, NotiActivity.class);
            startActivity(intent);
        });

    }

    private void loadUserData(String username) {
        userReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Users user = userSnapshot.getValue(Users.class);
                        if (user != null) {

                            usernameTextView.setText(user.getUsername());
                            nameTextView.setText(user.getName());
                            moneyTextView.setText("Số dư: " + String.valueOf(user.getMoney()));

                            Picasso.get().load(user.getAvatar()).into(avatarImageView);
                        }
                    }
                } else {
                    Toast.makeText(UserActivity.this, "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserActivity.this, "Lỗi khi tải dữ liệu người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        startActivity(new Intent(UserActivity.this, LoginActivity.class));
        finish();
    }
}
