package com.example.cse_project;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);

        // Thiết lập sự kiện click cho các liên kết
        TextView linkSachMot = findViewById(R.id.linkSachMot);
        TextView linkMasanoriSaki = findViewById(R.id.linkMasanoriSaki);
        TextView linkAnother = findViewById(R.id.linkAnother);

        linkSachMot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage("https://www.facebook.com/sach.mot.31521");
            }
        });

        linkMasanoriSaki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage("https://www.facebook.com/Masanori.Saki.18");
            }
        });

        linkAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail("support@example.com");
            }
        });

        // Thiết lập sự kiện click cho Bottom Navigation
        ImageView mainActivity = findViewById(R.id.mainactivity);
        ImageView categoryActivity = findViewById(R.id.categoryactivity);
        ImageView cartActivity = findViewById(R.id.cartactivity);
        ImageView userActivity = findViewById(R.id.useractivity);

        mainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this, MainActivity.class));
            }
        });

        categoryActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this, CategoryActivity.class));
            }
        });

        cartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this, CartActivity.class));
            }
        });

        userActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this, UserActivity.class));
            }
        });
    }

    // Phương thức mở liên kết web
    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    // Phương thức gửi email
    private void sendEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + email));
        startActivity(Intent.createChooser(emailIntent, "Gửi email..."));
    }
}

