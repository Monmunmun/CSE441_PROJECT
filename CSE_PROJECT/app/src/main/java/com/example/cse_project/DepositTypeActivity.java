package com.example.cse_project;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DepositTypeActivity extends AppCompatActivity {

    private Button buttonRechargeCard;
    private Button buttonRechargeBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_type);


        buttonRechargeCard = findViewById(R.id.buttonRechargeCard);
        buttonRechargeBank = findViewById(R.id.buttonRechargeBank);
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());


        buttonRechargeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DepositTypeActivity.this, DepositCardActivity.class);
                startActivity(intent);
            }
        });


        buttonRechargeBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DepositTypeActivity.this, DepositBankActivity.class);
                startActivity(intent);
            }
        });
    }
}
