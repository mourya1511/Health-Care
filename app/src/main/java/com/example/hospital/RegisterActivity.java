package com.example.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextUsername, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewLoginLink;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize EditText fields
        editTextEmail = findViewById(R.id.editTextAppFullName);
        editTextUsername = findViewById(R.id.editTextAppAddress);
        editTextPassword = findViewById(R.id.editTextAppContactNumber);
        editTextConfirmPassword = findViewById(R.id.editTextAppFees);

        // Initialize Register button
        buttonRegister = findViewById(R.id.buttonAppBack);
        buttonRegister.setOnClickListener(v -> registerUser());

        // Initialize TextView for login link
        textViewLoginLink = findViewById(R.id.textViewLoginLink);
        textViewLoginLink.setOnClickListener(v -> navigateToLogin());

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(getApplicationContext());
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(email)) {
            showToast("Enter email address!");
            return;
        }

        if (!isValidEmail(email)) {
            showToast("Enter valid email address (e.g., user@gmail.com)!");
            return;
        }

        if (TextUtils.isEmpty(username)) {
            showToast("Enter username!");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("Enter password!");
            return;
        }

        if (!isPasswordValid(password)) {
            showToast("Password must contain at least one uppercase letter, one symbol, and one numeric digit!");
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            showToast("Confirm your password!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match!");
            return;
        }

        // Register user in the database
        long result = dbHelper.registerUser(username, email, password);
        if (result == -1) {
            showToast("Registration failed. Please try again later.");
        } else {
            showToast("Registration successful!");
            navigateToLogin();
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean isPasswordValid(String password) {
        // Password must contain at least one uppercase letter, one symbol, and one numeric digit
        String passwordPattern = "^(?=.*[A-Z])(?=.*[@#$%^&+=])(?=.*[0-9]).*$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
