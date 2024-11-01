package com.example.cse_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    EditText searchInput;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bestSellersRecyclerView = findViewById(R.id.top_best_sellers_list);
        newLiteratureRecyclerView = findViewById(R.id.new_literature_books_list);

        searchInput = findViewById(R.id.search_input);
        searchButton = findViewById(R.id.search_button);

        bestSellersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        newLiteratureRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        bestSellersList = new ArrayList<>();
        newLiteratureList = new ArrayList<>();
        bestSellersAdapter = new BookAdapter(this, bestSellersList);
        newLiteratureAdapter = new BookAdapter(this, newLiteratureList);

        bestSellersRecyclerView.setAdapter(bestSellersAdapter);
        newLiteratureRecyclerView.setAdapter(newLiteratureAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Book");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bestSellersList.clear();
                newLiteratureList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Books book = postSnapshot.getValue(Books.class);
                    if (book != null) {
                        bestSellersList.add(book);
                        newLiteratureList.add(book);
                    }
                }
                bestSellersAdapter.notifyDataSetChanged();
                newLiteratureAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (!isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        searchButton.setOnClickListener(v -> {
            String keyword = searchInput.getText().toString().trim();
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("keyword", keyword);
            startActivity(intent);
        });

        ImageView userProfile = findViewById(R.id.useractivity);
        userProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            startActivity(intent);
        });

        ImageView categoryIcon = findViewById(R.id.categoryactivity);
        categoryIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            startActivity(intent);
        });

        ImageView cartIcon = findViewById(R.id.cartactivity);
        cartIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        ImageView helpIcon = findViewById(R.id.hotro);
        helpIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HelpActivity.class)));
    }
}
