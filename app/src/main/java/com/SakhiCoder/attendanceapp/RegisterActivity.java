package com.SakhiCoder.attendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class RegisterActivity extends AppCompatActivity {
    TextView btn;
    Button btnRegister;
    LoginDBHelper DB;
    private EditText InputUsername, InputEmail, InputPassword, InputComformPassword;

    //    String emailPatterns = "[a-zA-Zo-9._=]+@[a-z]+//.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InputUsername = findViewById(R.id.InputUserName);
        InputEmail = findViewById(R.id.inputEmail);
        InputPassword = findViewById(R.id.inputPassword);
        InputComformPassword = findViewById(R.id.inputComformPassword);
        btnRegister = findViewById(R.id.btnRegister);
        DB = new LoginDBHelper(this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCrededentils();
            }
        });
        btn = findViewById(R.id.alreadyHaveAccount);
        btn.setOnClickListener((v) -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void checkCrededentils() {
        String user = InputUsername.getText().toString();
        String email = InputEmail.getText().toString();
        String pass = InputPassword.getText().toString();
        String repass = InputComformPassword.getText().toString();

        if (user.equals("") || pass.equals("") || repass.equals(""))
            Toast.makeText(RegisterActivity.this, "Please enter the all fields", Toast.LENGTH_SHORT).show();
        else {
            if (pass.equals(repass)) {
                Boolean checkuser = DB.checkusername(user);
                if (checkuser == false) {
                    Boolean insert = DB.insertData(user, pass, email);
                    if (insert == true) {
                        Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, " Registration Failed ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "User already exists! Please Login", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Password not matching!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


//    String username=InputUsername.getText().toString();
//    String Email=InputEmail.getText().toString().trim();
//    String password=InputPassword.getText().toString();
//    String ComformPassword=InputComformPassword.getText().toString();
//
//        if (username.isEmpty() || username.length()<6){

//        showError(InputUsername,"Your user name is not valid!");
//        } else if (Email.isEmpty() || !Email.contains("@")) {
//        showError(InputEmail,"Email is not valid!");
//        } else if (password.isEmpty() || password.length()<6) {
//        showError(InputPassword,"Password must be 7 character!");
//        } else if (ComformPassword.isEmpty() || !ComformPassword.equals(password)) {
//        showError(InputComformPassword,"Conform Password is not match!");
//        } else {
//        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
//        Toast.makeText(this, "Registration Successfully!", Toast.LENGTH_SHORT).show();
//        }
//        }
//
//private void showError(EditText input, String s) {
//        input.setError(s);
//        input.requestFocus();
//        }