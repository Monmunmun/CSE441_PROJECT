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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_activity);

        bookImageView = findViewById(R.id.book_image);
        titleTextView = findViewById(R.id.book_title);
        authorTextView = findViewById(R.id.book_author);
        priceTextView = findViewById(R.id.book_price);
        backButton = findViewById(R.id.back_button);
        addToCartButton = findViewById(R.id.addtocartbutton);

        backButton.setOnClickListener(v -> finish());

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String price = intent.getStringExtra("price");
        String bookKey = intent.getStringExtra("key"); // Nhận key từ Books

        // Hiển thị dữ liệu
        titleTextView.setText(title);
        authorTextView.setText("Tác giả: " + author);
        priceTextView.setText("Giá: " + price + " VND");
        Picasso.get().load(imageUrl).into(bookImageView);

        // Khởi tạo Firebase DatabaseReference cho giỏ hàng
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null); // Lấy username từ SharedPreferences
        cartReference = FirebaseDatabase.getInstance().getReference("Cart").child(username); // Tạo cấu trúc giỏ hàng theo username

        // Xử lý sự kiện cho nút "Cho vào giỏ hàng"
        addToCartButton.setOnClickListener(v -> addToCart(bookKey, title, imageUrl, Integer.parseInt(price)));
    }

    private void addToCart(String bookKey, String title, String imageUrl, int price) {
        if (title == null || imageUrl == null || price <= 0) {
            Toast.makeText(BookDetailActivity.this, "Thông tin sản phẩm không đầy đủ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo một đối tượng Cart
        Cart cartItem = new Cart(title, imageUrl, price, 1, System.currentTimeMillis());

        // Lưu vào Firebase với key của Book
        cartReference.child(bookKey).setValue(cartItem)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(BookDetailActivity.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("BookDetailActivity", "Error adding to cart: ", e);
                    Toast.makeText(BookDetailActivity.this, "Lỗi khi thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                });
    }
}
