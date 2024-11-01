package com.example.cse_project;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CategoryBookActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCategory;
    private BookAdapter bookAdapter;
    private List<Books> booksList;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_book_activity);

        recyclerViewCategory = findViewById(R.id.recycler_view_category);


        int numberOfColumns = 3;
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(this, numberOfColumns));


        Intent intent = getIntent();
        category = intent.getStringExtra("category");


        TextView categoryTextView = findViewById(R.id.categoryTextView);
        categoryTextView.setText(category);

        booksList = new ArrayList<>();
        loadBooksByCategory(category);


        BookAdapter bookAdapter = new BookAdapter(this, booksList);
        recyclerViewCategory.setAdapter(bookAdapter);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void loadBooksByCategory(String category) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Book");
        databaseReference.orderByChild("category").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                booksList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Books book = dataSnapshot.getValue(Books.class);
                    booksList.add(book);
                }
                bookAdapter = new BookAdapter(CategoryBookActivity.this, booksList);
                recyclerViewCategory.setAdapter(bookAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
