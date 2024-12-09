package com.example.hospital;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CartBuyMedicineActivity extends AppCompatActivity {

    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;
    TextView tvTotal;
    ListView lst;

    private DatePickerDialog datePickerDialog;
    private Button dateButton, btnCheckout, btnBack;
    private String[][] packages;
    private float totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_buy_medicine);

        dateButton = findViewById(R.id.buttonBMCartDate);
        btnCheckout = findViewById(R.id.buttonBMCartCheckout);
        btnBack = findViewById(R.id.buttonBMCartBack);
        tvTotal = findViewById(R.id.textViewBMCartTotalCost);
        lst = findViewById(R.id.listViewBMCart);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (username.isEmpty()) {
            // Redirect to login or handle the case when username is empty
        } else {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            ArrayList<String> dbData = db.getCartData(username, "medicine");

            totalAmount = 0;

            packages = new String[dbData.size()][5];

            for (int i = 0; i < dbData.size(); i++) {
                String arrData = dbData.get(i);
                String[] strData = arrData.split("\\$");
                packages[i][0] = strData[0];
                packages[i][4] = "Cost : " + strData[1] + "/-";
                totalAmount += Float.parseFloat(strData[1]);
            }

            tvTotal.setText("Total Cost : " + totalAmount);

            list = new ArrayList<>();
            for (int i = 0; i < packages.length; i++) {
                item = new HashMap<>();
                item.put("line1", packages[i][0]);
                item.put("line5", packages[i][4]);
                list.add(item);
            }

            sa = new SimpleAdapter(this, list, R.layout.multi_lines, new String[]{"line1", "line5"}, new int[]{R.id.line_a, R.id.line_e});
            lst.setAdapter(sa);
        }

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(CartBuyMedicineActivity.this, BuyMedicineActivity.class));
            finish();
        });

        btnCheckout.setOnClickListener(v -> {
            // Handle the checkout logic here
             Intent it = new Intent(CartBuyMedicineActivity.this, BuyMedicineBookActivity.class);
             it.putExtra("price", tvTotal.getText());
             it.putExtra("date", dateButton.getText());
             startActivity(it);
            finish();
        });

        initDatePicker();
        dateButton.setOnClickListener(v -> datePickerDialog.show());
    }

    private void initDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            month1++;
            String date = dayOfMonth + "/" + month1 + "/" + year1;
            dateButton.setText(date);
        }, year, month, day);
    }
}
