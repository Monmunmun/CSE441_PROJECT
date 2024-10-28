package com.example.cse_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {

    private ListView categoryListView;
    private String[] categories = {"Sách bán chạy", "Sách mới phát hành",
            "Sách sắp phát hành", "Hài hước", "Sách ngoại văn", "Sách kinh tế"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);

        categoryListView = findViewById(R.id.category_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.category_item, R.id.category_name, categories);
        categoryListView.setAdapter(adapter);


        ImageView userProfile = findViewById(R.id.useractivity);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });


        ImageView homeIcon = findViewById(R.id.mainactivity);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView cartIcon = findViewById(R.id.cartactivity);
        cartIcon.setOnClickListener(v -> startActivity(new Intent(CategoryActivity.this, CartActivity.class)));

    }

}
