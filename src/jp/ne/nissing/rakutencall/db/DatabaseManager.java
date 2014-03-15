package jp.ne.nissing.rakutencall.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import jp.ne.nissing.rakutencall.contacts.data.ContactsData;

public class DatabaseManager {
    private static DatabaseManager mInstance = null;

    private static final String DATABASE_NAME = "ignore.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "ignore_contacts";
    public static final String COL_ID = "_id";
    public static final String COL_TEL_NUMBER = "tel_number";
    public static final String COL_DISPLAYNAME = "display_name";
    public static final String COL_CONTACTS_ID = "contacts_id";

    private final Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public static DatabaseManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseManager(context);
        }
        return mInstance;
    }

    private DatabaseManager(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "";
            sql += "CREATE TABLE " + TABLE_NAME + "(" + COL_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT , " + COL_TEL_NUMBER
                    + " TEXT NOT NULL , " + COL_DISPLAYNAME + " TEXT ," + COL_CONTACTS_ID
                    + " TEXT " + ")";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

    }

    public synchronized DatabaseManager open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public synchronized void close() {
        dbHelper.close();
    }

    public synchronized boolean deleteAllContacts() {
        return db.delete(TABLE_NAME, null, null) > 0;
    }

    public synchronized boolean deleteTargetContact(ContactsData contact) {
        return db.delete(TABLE_NAME, COL_TEL_NUMBER + " = ?",
                new String[] { contact.getTelNumber() }) > 0;
    }

    public synchronized boolean updateTargetContact(ContactsData contact) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTACTS_ID, contact.getContactsId());
        values.put(COL_DISPLAYNAME, contact.getDisplayName());
        values.put(COL_TEL_NUMBER, contact.getTelNumber());
        return db.insertOrThrow(TABLE_NAME, null, values) > 0;
    }

    public synchronized Cursor getContact(ContactsData contact) {
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_TEL_NUMBER + " = ?",
                new String[] { contact.getTelNumber() });
    }

    public synchronized Cursor getContacts() {
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
