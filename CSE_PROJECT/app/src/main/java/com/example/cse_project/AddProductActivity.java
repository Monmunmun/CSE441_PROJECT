package com.example.cse_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imagePreview;
    private EditText editTextTitle, editTextAuthor, editTextPrice, editTextStock, editTextCategory;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        // Khởi tạo các View
        imagePreview = findViewById(R.id.imagePreview);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextStock = findViewById(R.id.editTextStock);
        editTextCategory = findViewById(R.id.editTextCategory);
        Button buttonSelectImage = findViewById(R.id.buttonSelectImage);
        Button buttonSaveProduct = findViewById(R.id.buttonSaveProduct);
        ImageView backButton = findViewById(R.id.back_button);


        // Khởi tạo Firebase references
        storageReference = FirebaseStorage.getInstance().getReference("images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Book");

        // Thiết lập sự kiện cho nút chọn ảnh
        buttonSelectImage.setOnClickListener(v -> openFileChooser());

        // Thiết lập sự kiện cho nút lưu sản phẩm
        buttonSaveProduct.setOnClickListener(v -> addProduct());

        backButton.setOnClickListener(v -> finish());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imagePreview.setImageURI(imageUri); // Hiển thị ảnh đã chọn
        }
    }

    private void addProduct() {
        if (imageUri != null) {
            // Tải ảnh lên Firebase Storage
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpeg");

            fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String title = editTextTitle.getText().toString().trim();
                    String author = editTextAuthor.getText().toString().trim();
                    String priceString = editTextPrice.getText().toString().trim();
                    String stockString = editTextStock.getText().toString().trim();
                    String category = editTextCategory.getText().toString().trim();

                    if (!title.isEmpty() && !author.isEmpty() && !priceString.isEmpty() && !stockString.isEmpty() && !category.isEmpty()) {
                        double price = Double.parseDouble(priceString);
                        int stock = Integer.parseInt(stockString);

                        // Tạo đối tượng sản phẩm và lưu vào Firebase
                        String key = databaseReference.push().getKey();
                        Product product = new Product(key, title, author, price, uri.toString(), stock, category);
                        databaseReference.child(key).setValue(product)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddProductActivity.this, "Sản phẩm đã được thêm", Toast.LENGTH_SHORT).show();
                                        finish(); // Quay lại Activity trước đó
                                    } else {
                                        Toast.makeText(AddProductActivity.this, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(AddProductActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(AddProductActivity.this, "Lỗi khi tải ảnh lên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(AddProductActivity.this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
        }
    }
}