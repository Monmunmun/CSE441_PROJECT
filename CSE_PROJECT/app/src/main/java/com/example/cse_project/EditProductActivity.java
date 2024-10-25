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
import com.squareup.picasso.Picasso;

public class EditProductActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextAuthor, editTextPrice, editTextStock, editTextCategory;
    private ImageView imagePreview;
    private Button buttonSelectImage, buttonSaveProduct;
    private Uri imageUri;

    private DatabaseReference databaseReference;
    private String productId;

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
        String title = intent.getStringExtra("TITLE");
        String author = intent.getStringExtra("AUTHOR");
        String price = intent.getStringExtra("PRICE");
        String stock = intent.getStringExtra("STOCK");
        String category = intent.getStringExtra("CATEGORY");
        String imageUrl = intent.getStringExtra("IMAGE_URL");

        // Điền dữ liệu vào các trường nhập liệu
        editTextTitle.setText(title);
        editTextAuthor.setText(author);
        editTextPrice.setText(price);
        editTextStock.setText(stock);
        editTextCategory.setText(category);
        Picasso.get().load(imageUrl).into(imagePreview);

        // Thiết lập Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Book");

        // Xử lý sự kiện nhấn nút chọn hình ảnh
        buttonSelectImage.setOnClickListener(v -> {
            // Mở trình chọn hình ảnh
            Intent intentImage = new Intent();
            intentImage.setType("image/*");
            intentImage.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intentImage, "Chọn hình ảnh"), 1);
        });

        // Xử lý sự kiện nhấn nút lưu sản phẩm
        buttonSaveProduct.setOnClickListener(v -> saveProduct());

        // Thiết lập sự kiện quay lại
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

        // Kiểm tra stock
        int stockValue;
        try {
            stockValue = Integer.parseInt(stock);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra price
        double priceValue;
        try {
            priceValue = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra imageUri
        String imageUrl = (imageUri != null) ? imageUri.toString() : null;
        if (imageUrl == null) {
            Toast.makeText(this, "Vui lòng chọn hình ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật dữ liệu sản phẩm trong Firebase
        Product product = new Product(productId, title, author, priceValue, imageUrl, stockValue, category);;
        databaseReference.child(productId).setValue(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditProductActivity.this, "Sản phẩm đã được cập nhật", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại màn hình trước
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
            imagePreview.setImageURI(imageUri); // Hiển thị hình ảnh đã chọn
        }
    }
}
