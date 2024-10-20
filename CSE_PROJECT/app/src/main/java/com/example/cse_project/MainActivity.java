package com.example.cse_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView bestSellersRecyclerView, newLiteratureRecyclerView;
    BookAdapter bestSellersAdapter, newLiteratureAdapter;
    List<Books> bestSellersList, newLiteratureList;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo RecyclerViews
        bestSellersRecyclerView = findViewById(R.id.top_best_sellers_list);
        newLiteratureRecyclerView = findViewById(R.id.new_literature_books_list);

        bestSellersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        newLiteratureRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Khởi tạo danh sách và adapters
        bestSellersList = new ArrayList<>();
        newLiteratureList = new ArrayList<>();
        bestSellersAdapter = new BookAdapter(this, bestSellersList);
        newLiteratureAdapter = new BookAdapter(this, newLiteratureList);

        bestSellersRecyclerView.setAdapter(bestSellersAdapter);
        newLiteratureRecyclerView.setAdapter(newLiteratureAdapter);

        // Kết nối tới Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Book");

        // Lấy dữ liệu từ Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bestSellersList.clear();
                newLiteratureList.clear(); // Xóa danh sách trước khi thêm mới

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Books book = postSnapshot.getValue(Books.class);
                    if (book != null) {
                        bestSellersList.add(book); // Thêm sách vào danh sách sách bán chạy
                        newLiteratureList.add(book); // Thêm sách vào danh sách sách văn học mới
                    }
                }

                // Cập nhật cả hai adapter
                bestSellersAdapter.notifyDataSetChanged();
                newLiteratureAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (!isLoggedIn) {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        ImageView userProfile = findViewById(R.id.useractivity);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
    }
}
