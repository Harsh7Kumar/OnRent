package com.myactivity.onrent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myactivity.onrent.model.User;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText userName, userEmail,userPassword, conformPassword;
    private AppCompatButton signUpButton, loginButton;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        conformPassword = findViewById(R.id.user_conform_password);
        signUpButton = findViewById(R.id.sign_up_button);
        loginButton = findViewById(R.id.have_accounts);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                } else if(userEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                } else if(userPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else if(userPassword.getText().toString().trim().equals(conformPassword.getText().toString().trim())) {
                    if(emailChecker(userEmail.getText().toString().trim())) {
                        createUser(userEmail.getText().toString().trim(),
                                userPassword.getText().toString().trim(),
                                userName.getText().toString().trim());
                    }

                }
                else {
                    Toast.makeText(SignUpActivity.this, "Enter valids details!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(userName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        } else if(userEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
        } else if(userPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else if(userPassword.getText().toString().trim().equals(conformPassword.getText().toString().trim())) {
            Toast.makeText(this, "Enter valid password", Toast.LENGTH_SHORT).show();
        } else {
            if(emailChecker(userEmail.getText().toString().trim())) {
                createUser(userEmail.getText().toString().trim(),userPassword.getText().toString().trim(),userName.getText().toString().trim());
            } else {
                Toast.makeText(this, "Ente valid details", Toast.LENGTH_SHORT).show();
            }
        }
    }
    boolean emailChecker(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    void createUser(String email, String password, String name) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                User user = new User(name,email);

                if(task.isSuccessful()) {

                    mRef.child("users").push().setValue(user);
                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                    finish();

                    Toast.makeText(SignUpActivity.this, "User Created Successfully...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}