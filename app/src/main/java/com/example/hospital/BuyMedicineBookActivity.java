package com.example.hospital;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BuyMedicineBookActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextPinCode, editTextAddress, editTextContact;
    private Button buttonBooking;
    private TextView textViewTitle, textViewPackageName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_book); // Ensure this matches your actual layout file

        // Initialize views
        textViewTitle = findViewById(R.id.TextViewTitle);
        textViewPackageName = findViewById(R.id.TextViewBMBPackageName);
        editTextFullName = findViewById(R.id.EditTextBMBFullName);
        editTextPinCode = findViewById(R.id.EditTextBMBPinCode);
        editTextAddress = findViewById(R.id.EditTextBMBAddress);
        editTextContact = findViewById(R.id.EditTextBMBContact);
        buttonBooking = findViewById(R.id.ButtonBMBBooking);

        // Get data from Intent
        Intent intent = getIntent();
        String priceString = intent.getStringExtra("price");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");

        // Check for null or invalid price
        String[] price = priceString != null ? priceString.split(java.util.regex.Pattern.quote(":")) : new String[]{"0", "0"};

        // Set up button click listener
        buttonBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                    String username = sharedPreferences.getString("username", "");

                    try {
                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        db.addOrder(
                                username,
                                editTextFullName.getText().toString(),
                                editTextAddress.getText().toString(),
                                editTextContact.getText().toString(),
                                Integer.parseInt(editTextPinCode.getText().toString()),
                                date,
                                time,
                                Float.parseFloat(price[1]),
                                "medicine" // Use "medicine" as the type
                        );
                        db.removeCart(username, "medicine");
                        Toast.makeText(BuyMedicineBookActivity.this, "Booking Done successfully", Toast.LENGTH_SHORT).show();

                        // Redirect to HomeActivity
                        Intent homeIntent = new Intent(BuyMedicineBookActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(BuyMedicineBookActivity.this, "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateInputs() {
        String fullName = editTextFullName.getText().toString().trim();
        String pinCode = editTextPinCode.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();

        if (fullName.isEmpty()) {
            editTextFullName.setError("Full Name is required");
            return false;
        }
        if (pinCode.isEmpty()) {
            editTextPinCode.setError("Pin Code is required");
            return false;
        }
        if (address.isEmpty()) {
            editTextAddress.setError("Address is required");
            return false;
        }
        if (contact.isEmpty()) {
            editTextContact.setError("Contact Number is required");
            return false;
        }
        return true;
    }
}
