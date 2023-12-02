package com.SakhiCoder.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView name,btn_back;
    Button btn;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpass);


        name = findViewById(R.id.user_name);
        btn = findViewById(R.id.btnSend);
         btn_back = findViewById(R.id.backBtn);
        dbHelper = new DbHelper(this);
        btn_back.setOnClickListener(v -> backLogin());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String user = name.getText().toString();
//                Boolean checkName=dbHelper.che

            }
        });

    }

    private void backLogin() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}

