package com.example.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthArticlesActivity extends AppCompatActivity {

    private final String[][] healthDetails = {
            {"Walking Daily", "", "", "", "Click More Details"},
            {"Affects Drinking Alcohol", "", "", "", "Click More Details"},
            {"Stop Smoking", "", "", "", "Click More Details"},
            {"Menstrual Cramps", "", "", "", "Click More Details"},
            {"Healthy Gut", "", "", "", "Click More Details"}
    };

    private final int[] images = {
            R.drawable.health1,
            R.drawable.health2,
            R.drawable.health3,
            R.drawable.health4,
            R.drawable.health5
    };

    private List<HashMap<String, String>> itemList;
    private SimpleAdapter adapter;

    private Button btnBack;
    private ListView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_articles);

        lst = findViewById(R.id.listViewHA);
        btnBack = findViewById(R.id.backHAButton);

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(HealthArticlesActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        itemList = new ArrayList<>();
        for (String[] details : healthDetails) {
            HashMap<String, String> item = new HashMap<>();
            item.put("line1", details[0]);
            item.put("line2", details[1]);
            item.put("line3", details[2]);
            item.put("line4", details[3]);
            item.put("line5", details[4]);
            itemList.add(item);
        }

        adapter = new SimpleAdapter(this, itemList,
                R.layout.multi_lines,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});

        lst.setAdapter(adapter);

        lst.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent intent = new Intent(HealthArticlesActivity.this, HealthArticlesDetailsActivity.class);
            intent.putExtra("text1", healthDetails[position][0]);
            intent.putExtra("text2", images[position]);
            startActivity(intent);
        });
    }
}
