package com.example.cse_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import androidx.recyclerview.widget.GridLayoutManager;


public class SearchActivity extends AppCompatActivity {

    private EditText searchInput;
    private Button searchButton;
    private RecyclerView recyclerViewResults;
    private BookAdapter bookAdapter;
    private List<Books> booksList;

    private DatabaseReference booksReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        searchInput = findViewById(R.id.search_input);
        searchButton = findViewById(R.id.search_button);
        recyclerViewResults = findViewById(R.id.recyclerView_results);


        int numberOfColumns = 3;
        recyclerViewResults.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        booksList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, booksList);
        recyclerViewResults.setAdapter(bookAdapter);

        booksReference = FirebaseDatabase.getInstance().getReference("Book");

        String initialQuery = getIntent().getStringExtra("keyword");
        if (initialQuery != null && !initialQuery.isEmpty()) {
            searchInput.setText(initialQuery);
            performSearch(initialQuery);
        }

        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            performSearch(query);
        });

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void performSearch(String query) {
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
            return;
        }
        booksReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                booksList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot bookSnapshot : snapshot.getChildren()) {
                        Books book = bookSnapshot.getValue(Books.class);
                        if (book != null && book.getTitle() != null &&
                                book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                            booksList.add(book);
                        }
                    }
                    bookAdapter.notifyDataSetChanged();

                    if (booksList.isEmpty()) {
                        Toast.makeText(SearchActivity.this, "Không tìm thấy sách", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "Không tìm thấy sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, "Lỗi khi tìm kiếm sách", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
