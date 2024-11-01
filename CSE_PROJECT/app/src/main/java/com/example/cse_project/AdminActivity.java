package com.example.cse_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private ImageView logoutButton;
    private ImageView productButton;
    private ImageView notificationButton;
    private ImageView donhangButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        logoutButton = findViewById(R.id.logout_button);
        productButton = findViewById(R.id.sanpham);
        notificationButton = findViewById(R.id.thongbao);
        donhangButton = findViewById(R.id.donhang);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        productButton.setOnClickListener(new View.OnClickListener() { 
            @Override
            public void onClick(View v) {
                goToProductActivity();
            }
        });
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAdminNotiActivity();
            }
        });
        donhangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOrderActivity();
            }
        });
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToProductActivity() {
        Intent intent = new Intent(AdminActivity.this, ProductActivity.class);
        startActivity(intent);
    }
    private void goToAdminNotiActivity() {
        Intent intent = new Intent(AdminActivity.this, AdminNotiActivity.class);
        startActivity(intent);
    }
    private void goToOrderActivity() {
        Intent intent = new Intent(AdminActivity.this, AdminOrderActivity.class);
        startActivity(intent);
    }
}
