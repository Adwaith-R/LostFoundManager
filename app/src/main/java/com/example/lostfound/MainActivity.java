
package com.example.lostfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    TextView lostCount, foundCount;
    Button reportLost, reportFound, viewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        lostCount = findViewById(R.id.lostCount);
        foundCount = findViewById(R.id.foundCount);

        reportLost = findViewById(R.id.reportLost);
        reportFound = findViewById(R.id.reportFound);
        viewItems = findViewById(R.id.viewItems);

        reportLost.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("type", "Lost");
            startActivity(intent);
        });

        reportFound.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("type", "Found");
            startActivity(intent);
        });

        viewItems.setOnClickListener(v -> {
            startActivity(new Intent(this, ItemsActivity.class));
        });
    }

    private void updateCounts() {
        int lost = databaseHelper.getCount("Lost");
        int found = databaseHelper.getCount("Found");

        lostCount.setText(String.valueOf(lost));
        foundCount.setText(String.valueOf(found));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            updateCounts();
        }
    }
}
