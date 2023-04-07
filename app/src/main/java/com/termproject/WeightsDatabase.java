package com.termproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;


import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class WeightsDatabase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "WeightTracker.db";
    private static WeightsDatabase instance;

    private WeightsDatabase(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static WeightsDatabase getInstance(Context context){
        if (instance == null){
            instance = new WeightsDatabase(context);
        }
        return instance;
    }

    public static final class LoginTable {
        private static final String TABLE = "login";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";
        private static final String COL_GOAL = "goal";
    }

    public static final class WeightsTable{
        private static final String TABLE = "weights";
        private static final String COL_ID = "_id";
        private static final String COL_WEIGHT = "weight";
        private static final String COL_DATE = "date";
        private static final String COL_USERNAME = "username";
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + LoginTable.TABLE + " (" +
                LoginTable.COL_USERNAME + " primary key, " +
                LoginTable.COL_PASSWORD + ", " +
                LoginTable.COL_GOAL + " float" +
                ")");
        db.execSQL("create table " + WeightsTable.TABLE + " (" +
                WeightsTable.COL_ID + " integer primary key autoincrement, " +
                WeightsTable.COL_WEIGHT + " float, " +
                WeightsTable.COL_DATE + ", " +
                WeightsTable.COL_USERNAME + ", " +
                "foreign key(" + WeightsTable.COL_USERNAME + ") references " +
                LoginTable.TABLE + "(" + LoginTable.COL_USERNAME + ") on delete cascade)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists " + LoginTable.TABLE);
        db.execSQL("drop table if exists " + WeightsTable.TABLE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                db.execSQL("pragma foreign_keys = on;");
            } else {
                db.setForeignKeyConstraintsEnabled(true);
            }
        }
    }

    /**
     * If the account and password match - true/else false
     */
    public boolean validateLogin(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + LoginTable.TABLE + " where " +
                LoginTable.COL_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {username});

        if (cursor.moveToFirst()) {
            if (cursor.getString(0).equals(username)) {
                if (cursor.getString(1).equals(password)) {
                    // both match, login
                    return true;
                }
            }
        }
        // account does not exist || bad username || bad password
        return false;
    }

    /**
     * If the account and password match - true/else false
     */
    public boolean createAccount(String username, String password){
        // check that account already exists
        if (validateLogin(username, password)){
            return false;
        }
        // build a new account
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LoginTable.COL_USERNAME, username);
        values.put(LoginTable.COL_PASSWORD, password);
        long id = db.insert(LoginTable.TABLE, null, values);
        return id != -1;
    }

    public float getGoal(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + LoginTable.TABLE +
                " where " + LoginTable.COL_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { username });

        if (cursor.moveToFirst()) {
            // return the goal of the login entry
            return cursor.getFloat(2);
        }
        else{
            throw new RuntimeException("invalid username");
        }
    }

    public void setGoal(float goal, String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + LoginTable.TABLE +
                " where " + LoginTable.COL_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { username });

        ContentValues values = new ContentValues();
        if (cursor.moveToFirst()) {
            // get the original password
            String password = cursor.getString(1);

            // set the new values
            values.put(LoginTable.COL_USERNAME, username);
            values.put(LoginTable.COL_PASSWORD, password);
            values.put(LoginTable.COL_GOAL, goal);
            db.update(LoginTable.TABLE, values,
                    LoginTable.COL_USERNAME + " = " + username, null);
        }
        else{
            throw new RuntimeException("invalid username");
        }
    }


    /**
     * If the account and password match - true/else false
     */
    public List<Entry> getEntries(String username){
        List<Entry> entries = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from " + WeightsTable.TABLE +
                " where " + WeightsTable.COL_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { username });
        if (cursor.moveToFirst()) {
            do {
                Entry entry = new Entry();
                entry.setId(cursor.getInt(0));
                entry.setWeight(cursor.getFloat(1));
                entry.setDate(cursor.getString(2));
                entry.setUsername(cursor.getString(3));
                entries.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return entries;
    }

    public Entry getEntry(String date, String username){
        // this is used to check existence of an entry
        Entry entry = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + WeightsTable.TABLE +
                " where " + WeightsTable.COL_DATE + " = ? AND " + WeightsTable.COL_USERNAME +
                " = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { date, username });

        if (cursor.moveToFirst()) {
            entry = new Entry();
            entry.setId(cursor.getInt(0));
            entry.setDate(cursor.getString(1));
            entry.setWeight(cursor.getFloat(2));
            entry.setUsername(cursor.getString(3));
        }

        return entry;
    }

    public void addEntry(Entry entry){

        Entry existingEntry = getEntry(entry.getDate(), entry.getUsername());

        if (existingEntry != null){
            // an entry exists -> edit instead of add

            // set the id of the one to edit with the existing
            entry.setId(existingEntry.getId());
            // update the entry
            editEntry(entry);
        }
        else{
            // entry does not exits -> add entry
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(WeightsTable.COL_DATE, entry.getDate());
            values.put(WeightsTable.COL_WEIGHT, entry.getWeight());
            values.put(WeightsTable.COL_USERNAME, entry.getUsername());

            db.insert(WeightsTable.TABLE, null, values);
        }
    }

    public void editEntry(Entry entry){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WeightsTable.COL_ID, entry.getId());
        values.put(WeightsTable.COL_DATE, entry.getDate());
        values.put(WeightsTable.COL_WEIGHT, entry.getWeight());
        values.put(WeightsTable.COL_USERNAME, entry.getUsername());
        db.update(WeightsTable.TABLE, values,
                WeightsTable.COL_ID + " = " + entry.getId(), null);
    }

    public void removeEntry(Entry entry){
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(WeightsTable.TABLE, WeightsTable.COL_DATE + " = ?" +
                        " AND " + WeightsTable.COL_USERNAME + " = ?",
                new String[] {entry.getDate(), entry.getUsername() });
    }
}
