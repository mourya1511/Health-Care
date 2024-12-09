package com.example.hospital;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class OrderDetailsActivity extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> list;
    private SimpleAdapter sa;
    private ListView lst;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        btn = findViewById(R.id.buttonODBack);
        lst = findViewById(R.id.listViewOD);

        btn.setOnClickListener(view -> startActivity(new Intent(OrderDetailsActivity.this, HomeActivity.class)));

        // Initialize the list
        list = new ArrayList<>();

        // Load order data from the database in a background thread
        new LoadOrderDataTask().execute();

        // Handle window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private class LoadOrderDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedpreferences.getString("username", "");

            ArrayList<String> dbData = db.getOrderDate(username);
            String[][] order_details = new String[dbData.size()][];

            for (int i = 0; i < order_details.length; i++) {
                order_details[i] = new String[5];
                String arrData = dbData.get(i);
                String[] strData = arrData.split(Pattern.quote("$"));
                order_details[i][0] = strData[0];
                order_details[i][1] = strData[1];
                order_details[i][2] = "Rs. " + strData[2];
                order_details[i][3] = strData[6].equals("medicine") ? "Del: " + strData[4] : "Del: " + strData[4] + " " + strData[5];
                order_details[i][4] = strData[6];
            }

            for (String[] orderDetail : order_details) {
                HashMap<String, String> item = new HashMap<>();
                item.put("line1", orderDetail[0]);
                item.put("line2", orderDetail[1]);
                item.put("line3", orderDetail[2]);
                item.put("line4", orderDetail[3]);
                item.put("line5", orderDetail[4]);
                list.add(item);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            sa = new SimpleAdapter(OrderDetailsActivity.this, list,
                    R.layout.multi_lines,
                    new String[]{"line1", "line2", "line3", "line4", "line5"},
                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
            lst.setAdapter(sa);
        }
    }
}
