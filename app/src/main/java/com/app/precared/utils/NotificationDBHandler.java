package com.app.precared.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.precared.models.NotificationRow;

import java.util.ArrayList;


/**
 * Created by prashant on 28/4/16.
 */
public class NotificationDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NotificationDB";
    private static final String TABLE_Notification = "Notification";

    // NotificationDbRows Table Columns names
    private static final String KEY_NotificationID = "id";
    private static final String KEY_NotificationNAME = "Title";
    private static final String KEY_Notification_DESC = "Desc";
    private static final String KEY_Notification_SUBTYPE = "subtype";
    private static final String KEY_Notification_DATE = "date";

    public NotificationDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NotificationDbRowS_TABLE = "CREATE TABLE " + TABLE_Notification + "("
                + KEY_NotificationID + " INTEGER PRIMARY KEY," + KEY_NotificationNAME + " TEXT,"+ KEY_Notification_DESC + " TEXT,"+KEY_Notification_SUBTYPE + " TEXT,"
                + KEY_Notification_DATE + " TEXT" + ")";
        db.execSQL(CREATE_NotificationDbRowS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Notification);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new Notification row
    public void addNotification(NotificationRow notificationRow) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NotificationNAME, notificationRow.title);
        values.put(KEY_Notification_DESC, notificationRow.desc);
        values.put(KEY_Notification_SUBTYPE, notificationRow.subType);
        values.put(KEY_Notification_DATE, notificationRow.dateText);

        if (!isNotificationAvailable(notificationRow.id)) {
            // Inserting Row
            db.insert(TABLE_Notification, null, values);
        }
        db.close(); // Closing database connection
    }

    // Getting single Notiifcation
    public NotificationRow getNotification(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_Notification, new String[]{
                        KEY_NotificationNAME, KEY_Notification_DESC, KEY_Notification_SUBTYPE}, KEY_NotificationID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        NotificationRow GetLinkNotification = new NotificationRow(
                cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));
        // return GetLinkNotification
        return GetLinkNotification;
    }

    // Getting single Notification
    public boolean isNotificationAvailable(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_Notification, new String[]{KEY_NotificationID,
                        KEY_NotificationNAME, KEY_Notification_DESC,KEY_Notification_SUBTYPE,KEY_Notification_DATE}, KEY_NotificationID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            return cursor.getCount() > 0 ? true : false;


        return false;
    }

    // Getting All NotificationDbRows
    public ArrayList<NotificationRow> getAllNotifications() {
        ArrayList<NotificationRow> notificationRowList = new ArrayList<NotificationRow>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Notification;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NotificationRow notificationRow = new NotificationRow(
                        cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));
                // Adding GetLinkNotification to list
                notificationRowList.add(notificationRow);
            } while (cursor.moveToNext());
        }

        // return GetLinkNotification list
        return notificationRowList;
    }

    // Updating single GetLinkNotification
    public int updateNotificationRow(NotificationRow notificationRow) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NotificationNAME, notificationRow.title);
        values.put(KEY_Notification_DESC, notificationRow.desc); 
        values.put(KEY_Notification_SUBTYPE, notificationRow.subType); 
        values.put(KEY_Notification_DATE, notificationRow.dateText); 

        // updating row
        return db.update(TABLE_Notification, values, KEY_NotificationID + " = ?",
                new String[]{String.valueOf(notificationRow.id)});
    }
    
}