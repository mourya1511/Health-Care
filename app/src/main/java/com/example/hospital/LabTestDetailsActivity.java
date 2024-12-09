package com.example.hospital;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LabTestDetailsActivity extends AppCompatActivity {

    TextView tvPackageName, tvTotalCost;
    EditText edDetails;
    Button btnAddToCart, btnBack;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_details);

        tvPackageName = findViewById(R.id.textViewLDPackageName);
        tvTotalCost = findViewById(R.id.textViewLDTotalCost);
        edDetails = findViewById(R.id.editTextLDTextMultiLine);
        btnAddToCart = findViewById(R.id.buttonLDAddToCart);
        btnBack = findViewById(R.id.buttonLDBack);

        edDetails.setKeyListener(null);
        dbHelper = new DatabaseHelper(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            String packageName = intent.getStringExtra("text1");
            String details = intent.getStringExtra("text2");
            String totalCost = intent.getStringExtra("text3");

            tvPackageName.setText(packageName);
            edDetails.setText(details);
            tvTotalCost.setText("Total Cost : " + totalCost + "/-");
        }

        btnBack.setOnClickListener(v -> startActivity(new Intent(LabTestDetailsActivity.this, LabTestActivity.class)));

        btnAddToCart.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String product = tvPackageName.getText().toString();
            float price = Float.parseFloat(intent.getStringExtra("text3"));

            // Check if the product is already in the cart
            if (dbHelper.checkCart(username, product) == 1) {
                Toast.makeText(getApplicationContext(), "Product Already Added", Toast.LENGTH_SHORT).show();
            } else {
                long result = dbHelper.addCart(username, product, price, "lab");
                if (result != -1) {
                    Toast.makeText(getApplicationContext(), "Record Inserted to Cart", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LabTestDetailsActivity.this, LabTestActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to Insert Record", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
