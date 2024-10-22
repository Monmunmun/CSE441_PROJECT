package com.example.cse_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {

    private ListView categoryListView;
    private String[] categories = {"Sách bán chạy", "Kho sách giảm giá", "Sách mới phát hành",
            "Sách sắp phát hành", "Combo ưu đãi", "Sách ngoại văn", "Sách kinh tế"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);

        categoryListView = findViewById(R.id.category_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.category_item, R.id.category_name, categories);
        categoryListView.setAdapter(adapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedCategory = categories[position];
                Toast.makeText(CategoryActivity.this, "Đã chọn: " + selectedCategory, Toast.LENGTH_SHORT).show();

            }
        });

        // Xử lý sự kiện cho userProfile
        ImageView userProfile = findViewById(R.id.useractivity);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện cho homeIcon
        ImageView homeIcon = findViewById(R.id.mainactivity);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
