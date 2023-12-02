package com.SakhiCoder.attendanceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    TextView btn,forgot_btn;
    Button btnLogin ,btnGoogle,btnFacebook;
    EditText username, password;
    LoginDBHelper DB;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        username = findViewById(R.id.InputUserName);
        password = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnSend);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);
        forgot_btn = findViewById(R.id.forgotPassword);
        btnGoogle.setOnClickListener(v -> btnFacebook());
        btnFacebook.setOnClickListener(v -> btnGoogle());

        DB=new LoginDBHelper(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });

        btn = findViewById(R.id.textViewSingup);
        btn.setOnClickListener((v) -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            Toast.makeText(this, "Welcome to Registration!", Toast.LENGTH_SHORT).show();
        });
        forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, "Welcome To Re_Set Password!", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void btnGoogle() {
        Toast.makeText(this, "Sorry Working On it!", Toast.LENGTH_SHORT).show();
    }

    private void btnFacebook() {
        Toast.makeText(this, "Sorry Working On it!", Toast.LENGTH_SHORT).show();
    }

    private void checkCredentials() {
        String user=username.getText().toString();
        String pass=password.getText().toString();

        if (user.isEmpty( ) || pass.equals(""))
            Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        else {
            Boolean checkuserpass = DB.checkusernamepassword(user, pass);
            if (checkuserpass==true){
                Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(LoginActivity.this, " Please Register,Account not Exists! " +
                        " ---------------------------OR------------------------ " +
                        "  You are entering Wrong User name and Password!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
//    String name=username.getText()
//        String password=InputPassword.getText().toString();
//
//       if (Email.isEmpty() || !Email.contains("@")) {
//            showError(InputEmail,"Email is not valid!");
//        } else if (password.isEmpty() || password.length()<6) {
//            showError(InputPassword,"Password must be 7 character!");
//
//        } else {
//           startActivity(new Intent(LoginActivity.this,MainActivity.class));
//            Toast.makeText(this, "Login Successfully!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void showError(EditText input, String s) {
//        input.setError(s);
//        input.requestFocus();

