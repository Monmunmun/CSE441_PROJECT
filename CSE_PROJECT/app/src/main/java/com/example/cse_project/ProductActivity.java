package com.example.cse_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class ProductActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize product list and adapter
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList, this);
        productRecyclerView.setAdapter(productAdapter);

        // Reference to Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Book");

        // Load products from Firebase
        loadProducts();

        // Setup doanhthu ImageView click listener
        ImageView doanhthu = findViewById(R.id.doanhthu);
        doanhthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FloatingActionButton addProductButton = findViewById(R.id.addProductButton);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadProducts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onEditClick(int position) {
        Product product = productList.get(position);
        Intent intent = new Intent(ProductActivity.this, EditProductActivity.class);
        intent.putExtra("PRODUCT_ID", product.getKey());
        intent.putExtra("TITLE", product.getTitle());
        intent.putExtra("AUTHOR", product.getAuthor());
        intent.putExtra("PRICE", String.valueOf(product.getPrice()));
        intent.putExtra("STOCK", String.valueOf(product.getStock()));
        intent.putExtra("CATEGORY", product.getCategory());
        intent.putExtra("IMAGE_URL", product.getImageUrl());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        Product product = productList.get(position);
        String productId = product.getKey(); // Lấy ID sản phẩm để xóa

        if (productId != null) {
            // Xóa sản phẩm khỏi Firebase
            databaseReference.child(productId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ProductActivity.this, "Sản phẩm đã được xóa", Toast.LENGTH_SHORT).show();
                    productAdapter.removeItem(position); // Cập nhật danh sách sản phẩm
                } else {
                    Toast.makeText(ProductActivity.this, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
