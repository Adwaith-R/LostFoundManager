
package com.example.lostfound;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    TextView title;
    EditText itemName, description, location;
    Button submitButton;

    DatabaseHelper databaseHelper;
    String itemType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        databaseHelper = new DatabaseHelper(this);

        itemType = getIntent().getStringExtra("type");

        if (itemType == null) {
            itemType = "Lost";
        }

        title = findViewById(R.id.reportTitle);
        itemName = findViewById(R.id.itemName);
        description = findViewById(R.id.description);
        location = findViewById(R.id.location);
        submitButton = findViewById(R.id.submitButton);

        title.setText("Report " + itemType + " Item");

        submitButton.setOnClickListener(v -> saveReport());
    }

    private void saveReport() {

        String name = itemName.getText().toString().trim();
        String details = description.getText().toString().trim();
        String place = location.getText().toString().trim();

        if (name.isEmpty() || details.isEmpty() || place.isEmpty()) {
            Toast.makeText(this,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String date = new SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
        ).format(new Date());

        boolean inserted = databaseHelper.addItem(
                itemType,
                name,
                details,
                place,
                date
        );

        if (inserted) {
            Toast.makeText(this,
                    "Report submitted successfully!",
                    Toast.LENGTH_SHORT).show();

            finish();
        } else {
            Toast.makeText(this,
                    "Something went wrong!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
