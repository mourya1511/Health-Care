package com.example.hospital;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "healthcare.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createTableUsers = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "email TEXT," +
                "password TEXT)";
        db.execSQL(createTableUsers);

        // Create cart table
        String createTableCart = "CREATE TABLE cart (" +
                "username TEXT," +
                "product TEXT," +
                "price FLOAT," +
                "otype TEXT)";
        db.execSQL(createTableCart);

        // Create orders table
        String createTableOrder = "CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "fullname TEXT," +
                "address TEXT," +
                "contactno TEXT," +
                "pincode INT," +
                "date TEXT," +
                "time TEXT," +
                "price FLOAT," +
                "otype TEXT)";
        db.execSQL(createTableOrder);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed and recreate them
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS orders");
        onCreate(db);
    }

    public long addCart(String username, String product, float price, String otype) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("product", product);
        values.put("price", price);
        values.put("otype", otype);

        // Inserting Row
        long result = db.insert("cart", null, values);
        db.close(); // Closing database connection
        return result;
    }

    public int checkCart(String username, String product) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"username"};
        String selection = "username=? AND product=?";
        String[] selectionArgs = {username, product};

        Cursor cursor = null;
        try {
            cursor = db.query("cart", columns, selection, selectionArgs, null, null, null);
            return cursor != null && cursor.getCount() > 0 ? 1 : 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public int login(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id"};
        String selection = "username=? AND password=?";
        String[] selectionArgs = {username, password};

        Cursor cursor = null;
        try {
            cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
            return cursor != null && cursor.getCount() > 0 ? 1 : 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public long registerUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);

        // Inserting Row
        long result = db.insert("users", null, values);
        db.close(); // Closing database connection
        return result;
    }

    public void removeCart(String username, String otype) {
        String[] str = {username, otype};
        SQLiteDatabase db = getWritableDatabase();
        db.delete("cart", "username=? AND otype=?", str);
        db.close();
    }

    public ArrayList<String> getCartData(String username, String otype) {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] str = {username, otype};
        Cursor c = db.rawQuery("SELECT * FROM cart WHERE username = ? AND otype = ?", str);
        try {
            if (c.moveToFirst()) {
                do {
                    String product = c.getString(1);
                    String price = c.getString(2);
                    arr.add(product + "$" + price);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
            db.close();
        }
        return arr;
    }

    public long addOrder(String username, String fullname, String address, String contact, int pincode, String date, String time, float price, String otype) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("fullname", fullname);
        cv.put("address", address);
        cv.put("contactno", contact);
        cv.put("pincode", pincode);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("price", price);
        cv.put("otype", otype);

        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert("orders", null, cv);
        db.close();
        return result;
    }

    public ArrayList<String> getOrderData(String username) {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] str = {username};
        Cursor c = db.rawQuery("SELECT * FROM orders WHERE username = ?", str);
        try {
            if (c.moveToFirst()) {
                do {
                    arr.add(c.getString(1) + "$" + c.getString(2) + "$" + c.getString(3) + "$" + c.getString(4) + "$" + c.getString(5) + "$" + c.getString(6) + "$" + c.getString(7) + "$" + c.getString(8));
                } while (c.moveToNext());
            }
        } finally {
            c.close();
            db.close();
        }
        return arr;
    }

    public boolean checkAppointmentExists(String username, String fullname, String address, String contactno, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id"};
        String selection = "username=? AND fullname=? AND address=? AND contactno=? AND date=? AND time=?";
        String[] selectionArgs = {username, fullname, address, contactno, date, time};

        Cursor cursor = null;
        try {
            cursor = db.query("orders", columns, selection, selectionArgs, null, null, null);
            return cursor != null && cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public ArrayList<String> getOrderDate(String username) {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] str = {username};
        Cursor c = db.rawQuery("SELECT fullname, address, price, contactno, date, time, otype FROM orders WHERE username = ?", str);
        try {
            if (c.moveToFirst()) {
                do {
                    arr.add(c.getString(0) + "$" + c.getString(1) + "$" + c.getString(2) + "$" + c.getString(3) + "$" + c.getString(4) + "$" + c.getString(5) + "$" + c.getString(6));
                } while (c.moveToNext());
            }
        } finally {
            c.close();
            db.close();
        }
        return arr;
    }
    }


