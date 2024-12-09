package com.example.hospital;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CartLabActivity extends AppCompatActivity {
    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;
    TextView tvTotal;
    ListView lst;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton, timeButton, btnCheckout, btnBack;
    private String[][] packages;
    private float totalAmount; // Changed to float for correct calculation

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_lab);

        dateButton = findViewById(R.id.buttonCartDate);
        timeButton = findViewById(R.id.buttonCartTime);
        btnCheckout = findViewById(R.id.buttonCartCheckout);
        btnBack = findViewById(R.id.buttonCartBack);
        tvTotal = findViewById(R.id.textViewCartTotalCost);
        lst = findViewById(R.id.listViewCart);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Handle if username is empty or null
        if (username.isEmpty()) {
            // Handle this case, maybe redirect to login or set a default username
        } else {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            ArrayList<String> dbData = db.getCartData(username, "lab");

            totalAmount = 0; // Initialize totalAmount

            packages = new String[dbData.size()][5]; // Initialize packages array

            for (int i = 0; i < dbData.size(); i++) {
                String arrData = dbData.get(i).toString();
                String[] strData = arrData.split("\\$"); // Split by "$", escape regex special character
                packages[i][0] = strData[0]; // Assuming strData[0] is the name/package title
                packages[i][4] = "Cost : " + strData[1] + "/-"; // Assuming strData[1] is the cost

                totalAmount += Float.parseFloat(strData[1]); // Accumulate total cost
            }

            // Display total amount
            tvTotal.setText("Total Cost : " + totalAmount);

            // Prepare data for ListView
            list = new ArrayList<>();
            for (int i = 0; i < packages.length; i++) {
                item = new HashMap<>();
                item.put("line1", packages[i][0]);
                item.put("line5", packages[i][4]);
                list.add(item);
            }

            // Set up SimpleAdapter for ListView
            sa = new SimpleAdapter(this, list,
                    R.layout.multi_lines,
                    new String[]{"line1", "line5"},
                    new int[]{R.id.line_a, R.id.line_e});
            lst.setAdapter(sa);
        }

        // Back button click listener
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(CartLabActivity.this, LabTestActivity.class));
            finish();
        });

        btnCheckout.setOnClickListener(v -> {
            Intent it = new Intent(CartLabActivity.this,LabTestBookActivity.class);
            it.putExtra("price",tvTotal.getText());
            it.putExtra("date",dateButton.getText());
            it.putExtra("time",timeButton.getText());
            startActivity(it);
            finish();
        });

        // Initialize date and time pickers
        initDatePicker();
        initTimePicker();

        // Date and time button click listeners
        dateButton.setOnClickListener(v -> datePickerDialog.show());
        timeButton.setOnClickListener(v -> timePickerDialog.show());
    }

    private void initDatePicker() {
        // Set up date picker with a listener to update the button text
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            month = month + 1; // Month is 0-based, so add 1
            String date = dayOfMonth + "/" + month + "/" + year;
            dateButton.setText(date);
        };

        // Set minimum date to tomorrow
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1); // Adding 1 day to current date
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis()); // Set minimum date
    }

    private void initTimePicker() {
        // Set up time picker with a listener to update the button text
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            String time = String.format("%02d:%02d", hourOfDay, minute);
            timeButton.setText(time);
        };

        // Set current time as default
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(this, timeSetListener, hour, minute, true);
    }
}
