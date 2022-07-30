package com.example.mycalendar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Event.db";
    private static final String CREATE_TABLE = "CREATE TABLE event (ID TEXT PRIMARY KEY, START_DATE TEXT, END_DATE TEXT, START_TIME TEXT, END_TIME TEXT, TITLE TEXT, NOTE TEXT, COLOR TEXT)";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS event";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public Boolean insertEventData(String id, String start_date, String end_date, String start_time, String end_time, String title, String note, String color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ID", id);
        contentValues.put("START_DATE", start_date);
        contentValues.put("END_DATE", end_date);
        contentValues.put("START_TIME", start_time);
        contentValues.put("END_TIME", end_time);
        contentValues.put("TITLE", title);
        contentValues.put("NOTE", note);
        contentValues.put("COLOR", color);

        long result = db.insert("event", null, contentValues);
        if (result == -1) return false;
        else return true;
    }

//    public Boolean updateEventData(String id, String start_date, String end_date, String start_time, String end_time, String title, String note, String color){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put("ID", id);
//        contentValues.put("START_DATE", start_date);
//        contentValues.put("END_DATE", end_date);
//        contentValues.put("START_TIME", start_time);
//        contentValues.put("END_TIME", end_time);
//        contentValues.put("TITLE", title);
//        contentValues.put("NOTE", note);
//        contentValues.put("COLOR", color);
//
//        Cursor cursor = db.rawQuery("SELECT * FROM event WHERE ID=?", new String[] {id});
//        if (cursor.getCount() > 0) {
//            long result = db.update("event", contentValues, "id=?", new String[]{id});
//            if (result == -1) return false;
//            else return true;
//        }
//        else {
//            return false;
//        }
//    }

    public Boolean deleteEventData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
        long result = db.delete("event","id LIKE ?", new String[]{id.substring(0, id.length()-1) + "%"});
        if (result == -1) return false;
        else return true;
    }

    public Cursor getEventDataByDate(String[] date){
        SQLiteDatabase db = this.getReadableDatabase();
//        System.out.println(date[0]);
        Cursor cursor = db.rawQuery("SELECT * FROM event WHERE START_DATE=? ORDER BY START_TIME ASC", date);
        return cursor;
    }

    public Cursor getEventDataByID(String[] id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM event WHERE ID=?", id);
        return cursor;
    }

}
