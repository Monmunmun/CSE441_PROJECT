package com.example.cse_project;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;


public class CategoryActivity extends AppCompatActivity {

    private ListView categoryListView;
    private String[] categories = {"Tình yêu", "Hài hước", "Kinh dị", "Kì lạ", "Nông nghiệp", "Kinh tế", "Giải trí"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);

        categoryListView = findViewById(R.id.category_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.category_item, R.id.category_name, categories);
        categoryListView.setAdapter(adapter);


        categoryListView.setOnItemClickListener((parent, view, position, id) -> {

            String selectedCategory = categories[position];
            Intent intent = new Intent(CategoryActivity.this, CategoryBookActivity.class);
            intent.putExtra("category", selectedCategory);
            startActivity(intent);
        });

        ImageView userProfile = findViewById(R.id.useractivity);
        userProfile.setOnClickListener(v -> startActivity(new Intent(CategoryActivity.this, UserActivity.class)));

        ImageView homeIcon = findViewById(R.id.mainactivity);
        homeIcon.setOnClickListener(v -> startActivity(new Intent(CategoryActivity.this, MainActivity.class)));

        ImageView cartIcon = findViewById(R.id.cartactivity);
        cartIcon.setOnClickListener(v -> startActivity(new Intent(CategoryActivity.this, CartActivity.class)));

        ImageView helpIcon = findViewById(R.id.hotro);
        helpIcon.setOnClickListener(v -> startActivity(new Intent(CategoryActivity.this, HelpActivity.class)));
    }
}
