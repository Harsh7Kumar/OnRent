package com.myactivity.onrent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    private EditText email,passwaor;
    private  FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.user_email);
        passwaor = findViewById(R.id.user_password);
        AppCompatButton loginButton = findViewById(R.id.login_button);
        AppCompatButton registerButton = findViewById(R.id.dont_have_accounts);

        auth = FirebaseAuth.getInstance();


        registerButton.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            finish();
        });

        loginButton.setOnClickListener(view -> {
            if(!email.getText().toString().trim().isEmpty() && emailChecker(email.getText().toString().trim()) ) {
                if(!passwaor.getText().toString().isEmpty()) {
                    loginUser(email.getText().toString().trim(),passwaor.getText().toString().trim());

                } else {
                    Toast.makeText(LoginActivity.this, "Enter valid password!", Toast.LENGTH_SHORT).show();
                }
             } else {
                Toast.makeText(LoginActivity.this, "Enter valid Email!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean emailChecker(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    void loginUser(String email, String password) {

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show());
    }
}