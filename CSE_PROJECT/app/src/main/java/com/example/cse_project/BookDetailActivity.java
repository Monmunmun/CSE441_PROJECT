package com.example.cse_project;

import com.example.cse_project.Cart;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Button;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView bookImageView, backButton;
    private TextView titleTextView, authorTextView, priceTextView;
    private Button addToCartButton, sendReviewButton;
    private EditText reviewEditText;
    private RecyclerView reviewsRecyclerView;

    private DatabaseReference cartReference;
    private DatabaseReference commentsReference;
    private String username;

    private CommentAdapter commentAdapter;
    private List<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_activity);

        // Ánh xạ các view
        bookImageView = findViewById(R.id.book_image);
        titleTextView = findViewById(R.id.book_title);
        authorTextView = findViewById(R.id.book_author);
        priceTextView = findViewById(R.id.book_price);
        backButton = findViewById(R.id.back_button);
        addToCartButton = findViewById(R.id.addtocartbutton);
        sendReviewButton = findViewById(R.id.send_review_button);
        reviewEditText = findViewById(R.id.review_edit_text);
        reviewsRecyclerView = findViewById(R.id.reviewsrecyclerview);

        // Thiết lập RecyclerView cho bình luận
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(comments);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setAdapter(commentAdapter);

        backButton.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String price = intent.getStringExtra("price");
        String bookKey = intent.getStringExtra("key");

        titleTextView.setText(title);
        authorTextView.setText("Tác giả: " + author);
        priceTextView.setText("Giá: " + price + " VND");
        Picasso.get().load(imageUrl).into(bookImageView);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "default_username");

        cartReference = FirebaseDatabase.getInstance().getReference("carts").child(username);
        commentsReference = FirebaseDatabase.getInstance().getReference("comments").child(bookKey);

        addToCartButton.setOnClickListener(v -> {
            addToCart(imageUrl, title, Double.parseDouble(price), bookKey);
        });

        sendReviewButton.setOnClickListener(v -> {
            String reviewContent = reviewEditText.getText().toString().trim();
            if (!reviewContent.isEmpty()) {
                addComment(bookKey, reviewContent);
            } else {
                Toast.makeText(this, "Vui lòng nhập bình luận", Toast.LENGTH_SHORT).show();
            }
        });

        loadComments(bookKey);
    }

    private void addToCart(String imageUrl, String title, double price, String bookId) {
        Cart cartItem = new Cart(bookId, imageUrl, title, 1, price);

        cartReference.child("items").child(bookId).setValue(cartItem)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                });
    }

    private void addComment(String bookKey, String content) {
        long timestamp = System.currentTimeMillis();
        Comment comment = new Comment(bookKey, username, content, timestamp);

        commentsReference.push().setValue(comment)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Bình luận đã được gửi", Toast.LENGTH_SHORT).show();
                    reviewEditText.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi gửi bình luận", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadComments(String bookKey) {
        commentsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    if (comment != null) {
                        comments.add(comment);
                    }
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookDetailActivity.this, "Lỗi tải bình luận", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
