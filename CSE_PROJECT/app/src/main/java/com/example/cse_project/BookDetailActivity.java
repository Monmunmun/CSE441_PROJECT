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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView bookImageView, backButton;
    private TextView titleTextView, authorTextView, priceTextView;
    private Button addToCartButton;

    private DatabaseReference cartReference;
    private String username;

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


        addToCartButton.setOnClickListener(v -> {
            addToCart(imageUrl, title, Double.parseDouble(price), bookKey);
        });
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
}
