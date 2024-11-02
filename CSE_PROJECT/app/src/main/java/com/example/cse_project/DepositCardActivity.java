package com.example.cse_project;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DepositCardActivity extends AppCompatActivity {

    private Spinner spinnerCardType, spinnerAmount;
    private EditText editTextCardSeri, editTextCardCode;
    private Button buttonConfirmRecharge;

    private DatabaseReference depositsRef;
    private DatabaseReference usersRef;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_card);

        // Khởi tạo Firebase Database
        depositsRef = FirebaseDatabase.getInstance().getReference("deposits");
        usersRef = FirebaseDatabase.getInstance().getReference("User");


        spinnerCardType = findViewById(R.id.spinnerCardType);
        spinnerAmount = findViewById(R.id.spinnerAmount);
        editTextCardSeri = findViewById(R.id.editTextCardSeri);
        editTextCardCode = findViewById(R.id.editTextCardCode);
        buttonConfirmRecharge = findViewById(R.id.buttonConfirmRecharge);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());


        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);


        ArrayAdapter<CharSequence> cardTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.card_types_array, android.R.layout.simple_spinner_item);
        cardTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCardType.setAdapter(cardTypeAdapter);


        ArrayAdapter<CharSequence> amountAdapter = ArrayAdapter.createFromResource(this,
                R.array.amount_array, android.R.layout.simple_spinner_item);
        amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAmount.setAdapter(amountAdapter);

        buttonConfirmRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmRecharge();
            }
        });
    }

    private void confirmRecharge() {

        String cardType = spinnerCardType.getSelectedItem().toString();
        String amountStr = spinnerAmount.getSelectedItem().toString();
        String cardSeri = editTextCardSeri.getText().toString();
        String cardCode = editTextCardCode.getText().toString();

        if (cardSeri.isEmpty() || cardCode.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cardSeri.length() != 16) {
            Toast.makeText(this, "Vui lòng nhập đúng số seri", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cardCode.length() != 12) {
            Toast.makeText(this, "Vui lòng nhập đúng mã thẻ", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        String cardID = depositsRef.push().getKey();


        Deposit deposit = new Deposit(cardID, username, cardType, amount, cardSeri, cardCode, ""); // cardDate có thể để trống nếu không cần


        depositsRef.child(cardID).setValue(deposit)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        updateUserMoney(amount);
                        Toast.makeText(this, "Nạp tiền thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Lỗi khi nạp tiền.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserMoney(double amount) {
        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Users user = userSnapshot.getValue(Users.class);
                        if (user != null) {
                            double currentMoney = user.getMoney();

                            currentMoney += amount;
                            userSnapshot.getRef().child("money").setValue(currentMoney)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            Toast.makeText(DepositCardActivity.this, "Cập nhật số tiền thành công!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(DepositCardActivity.this, "Cập nhật số tiền thất bại!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                } else {
                    Toast.makeText(DepositCardActivity.this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DepositCardActivity.this, "Lỗi khi lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
