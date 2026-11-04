package com.example.lostfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LostFound.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type TEXT, " +
                "name TEXT, " +
                "description TEXT, " +
                "location TEXT, " +
                "date TEXT, " +
                "status TEXT)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS items");
        onCreate(db);
    }

    public boolean addItem(String type, String name,
                           String description, String location,
                           String date) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("name", name);
        values.put("description", description);
        values.put("location", location);
        values.put("date", date);
        values.put("status", "Pending");

        long result = db.insert("items", null, values);

        return result != -1;
    }

    public Cursor getAllItems() {
        return getItems(null);
    }

    public Cursor getItems(String keyword) {
        SQLiteDatabase db = this.getReadableDatabase();

        if (keyword == null || keyword.trim().isEmpty()) {
            return db.rawQuery(
                    "SELECT * FROM items ORDER BY id DESC",
                    null
            );
        }

        String searchPattern = "%" + keyword.trim() + "%";
        return db.rawQuery(
                "SELECT * FROM items WHERE type LIKE ? OR name LIKE ? OR description LIKE ? OR location LIKE ? ORDER BY id DESC",
                new String[]{searchPattern, searchPattern, searchPattern, searchPattern}
        );
    }

    public int getCount(String type) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM items WHERE type=?",
                new String[]{type}
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    public boolean deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                "items",
                "id=?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }
}
