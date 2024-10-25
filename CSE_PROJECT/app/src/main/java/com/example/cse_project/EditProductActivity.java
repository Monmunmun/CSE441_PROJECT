package com.example.cse_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EditProductActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextAuthor, editTextPrice, editTextStock, editTextCategory;
    private ImageView imagePreview;
    private Button buttonSelectImage, buttonSaveProduct;
    private Uri imageUri;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String productId, currentImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);

        // Khởi tạo các thành phần giao diện
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextStock = findViewById(R.id.editTextStock);
        editTextCategory = findViewById(R.id.editTextCategory);
        imagePreview = findViewById(R.id.imagePreview);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonSaveProduct = findViewById(R.id.buttonSaveProduct);

        // Nhận dữ liệu sản phẩm từ Intent
        Intent intent = getIntent();
        productId = intent.getStringExtra("PRODUCT_ID");
        currentImageUrl = intent.getStringExtra("IMAGE_URL");
        editTextTitle.setText(intent.getStringExtra("TITLE"));
        editTextAuthor.setText(intent.getStringExtra("AUTHOR"));
        editTextPrice.setText(intent.getStringExtra("PRICE"));
        editTextStock.setText(intent.getStringExtra("STOCK"));
        editTextCategory.setText(intent.getStringExtra("CATEGORY"));

        // Hiển thị hình ảnh hiện tại
        Picasso.get().load(currentImageUrl).into(imagePreview);

        // Khởi tạo Firebase Database và Storage
        databaseReference = FirebaseDatabase.getInstance().getReference("Book");
        storageReference = FirebaseStorage.getInstance().getReference("images");

        // Xử lý sự kiện chọn hình ảnh
        buttonSelectImage.setOnClickListener(v -> {
            Intent intentImage = new Intent();
            intentImage.setType("image/*");
            intentImage.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intentImage, "Chọn hình ảnh"), 1);
        });

        // Xử lý sự kiện lưu sản phẩm
        buttonSaveProduct.setOnClickListener(v -> saveProduct());

        // Quay lại
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void saveProduct() {
        String title = editTextTitle.getText().toString();
        String author = editTextAuthor.getText().toString();
        String price = editTextPrice.getText().toString();
        String stock = editTextStock.getText().toString();
        String category = editTextCategory.getText().toString();

        // Kiểm tra các trường nhập liệu
        if (title.isEmpty() || author.isEmpty() || price.isEmpty() || stock.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra stock và price
        int stockValue;
        double priceValue;
        try {
            stockValue = Integer.parseInt(stock);
            priceValue = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá hoặc số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu có ảnh mới, tải lên Storage
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
            fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String newImageUrl = uri.toString();
                        updateProductInDatabase(newImageUrl, title, author, priceValue, stockValue, category);
                    })
            ).addOnFailureListener(e ->
                    Toast.makeText(EditProductActivity.this, "Lỗi khi tải ảnh lên: " + e.getMessage(), Toast.LENGTH_SHORT).show()
            );
        } else {
            // Nếu không có ảnh mới, dùng URL ảnh cũ
            updateProductInDatabase(currentImageUrl, title, author, priceValue, stockValue, category);
        }
    }

    private void updateProductInDatabase(String imageUrl, String title, String author, double price, int stock, String category) {
        Product product = new Product(productId, title, author, price, imageUrl, stock, category);
        databaseReference.child(productId).setValue(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditProductActivity.this, "Sản phẩm đã được cập nhật", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditProductActivity.this, "Lỗi khi cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imagePreview.setImageURI(imageUri); // Hiển thị hình ảnh mới đã chọn
        }
    }
}
