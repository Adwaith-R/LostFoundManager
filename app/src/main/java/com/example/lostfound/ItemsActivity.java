package com.example.lostfound;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class ItemsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private EditText searchBox;
    private ListView itemsList;

    private ArrayList<String> itemDetails;
    private ArrayList<Integer> itemIds;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        databaseHelper = new DatabaseHelper(this);

        searchBox = findViewById(R.id.searchBox);
        itemsList = findViewById(R.id.itemsList);

        itemDetails = new ArrayList<>();
        itemIds = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                itemDetails
        );

        itemsList.setAdapter(adapter);
        itemsList.setEmptyView(findViewById(R.id.emptyMessage));

        loadItems("");

        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                loadItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        itemsList.setOnItemLongClickListener((parent, view, position, id) -> {
            if (position < 0 || position >= itemIds.size()) {
                return true;
            }

            int selectedId = itemIds.get(position);

            new AlertDialog.Builder(this)
                    .setTitle("Delete Report")
                    .setMessage("Do you want to delete this report?")
                    .setPositiveButton("Delete", (dialog, which) -> {

                        boolean deleted = databaseHelper.deleteItem(selectedId);

                        if (deleted) {
                            Toast.makeText(this,
                                    "Report deleted",
                                    Toast.LENGTH_SHORT).show();

                            loadItems(searchBox.getText().toString());
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

            return true;
        });
    }

    private void loadItems(String keyword) {

        itemDetails.clear();
        itemIds.clear();

        Cursor cursor = databaseHelper.getItems(keyword);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                int typeIndex = cursor.getColumnIndex("type");
                int nameIndex = cursor.getColumnIndex("name");
                int descIndex = cursor.getColumnIndex("description");
                int locIndex = cursor.getColumnIndex("location");
                int dateIndex = cursor.getColumnIndex("date");
                int statusIndex = cursor.getColumnIndex("status");

                do {
                    int id = idIndex != -1 ? cursor.getInt(idIndex) : -1;
                    String type = typeIndex != -1 && !cursor.isNull(typeIndex) ? cursor.getString(typeIndex) : "Unknown";
                    String name = nameIndex != -1 && !cursor.isNull(nameIndex) ? cursor.getString(nameIndex) : "";
                    String description = descIndex != -1 && !cursor.isNull(descIndex) ? cursor.getString(descIndex) : "";
                    String location = locIndex != -1 && !cursor.isNull(locIndex) ? cursor.getString(locIndex) : "";
                    String date = dateIndex != -1 && !cursor.isNull(dateIndex) ? cursor.getString(dateIndex) : "";
                    String status = statusIndex != -1 && !cursor.isNull(statusIndex) ? cursor.getString(statusIndex) : "";

                    String details =
                            type.toUpperCase(Locale.ROOT) + " ITEM\n" +
                                    "Name: " + name + "\n" +
                                    "Description: " + description + "\n" +
                                    "Location: " + location + "\n" +
                                    "Date: " + date + "\n" +
                                    "Status: " + status +
                                    "\n\nHold to delete";

                    itemIds.add(id);
                    itemDetails.add(details);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && searchBox != null) {
            loadItems(searchBox.getText().toString());
        }
    }
}
