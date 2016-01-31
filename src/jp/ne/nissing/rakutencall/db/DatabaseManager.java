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
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME_IGNORE_CONTACTS = "ignore_contacts";
    public static final String COL_ID = "_id";
    public static final String COL_TEL_NUMBER = "tel_number";
    public static final String COL_DISPLAYNAME = "display_name";
    public static final String COL_CONTACTS_ID = "contacts_id";
    
    public static final String TABLE_NAME_PREFIX = "prefix_numbers";

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
            db.execSQL("CREATE TABLE " + TABLE_NAME_IGNORE_CONTACTS + "(" + COL_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT , " + COL_TEL_NUMBER
                    + " TEXT NOT NULL , " + COL_DISPLAYNAME + " TEXT ," + COL_CONTACTS_ID
                    + " TEXT " + ")");
            
            db.execSQL("CREATE TABLE " + TABLE_NAME_PREFIX +
                    "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                    + COL_TEL_NUMBER + " TEXT NOT NULL" + ")");
            initPrefix(db);
            
            
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if( oldVersion == 1 && newVersion == 2){
                db.execSQL("CREATE TABLE " + TABLE_NAME_PREFIX +
                        "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                        + COL_TEL_NUMBER + " TEXT NOT NULL" + ")");
                initPrefix(db);

            }
        }
        
        private void initPrefix(SQLiteDatabase db){
            db.beginTransaction();
            try{
                String[] prefixs = new String[]{"0120" , "0077" , "0088" , "0570"};
                ContentValues values = new ContentValues(prefixs.length);

                for(String prefix : prefixs){
                    values.clear();
                    values.put(COL_TEL_NUMBER, prefix);
                    db.insertOrThrow(TABLE_NAME_PREFIX, null, values);
                }
                db.setTransactionSuccessful();
            } finally{
                db.endTransaction();
            }
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
        return db.delete(TABLE_NAME_IGNORE_CONTACTS, null, null) > 0;
    }

    public synchronized boolean deleteTargetContact(ContactsData contact) {
        return db.delete(TABLE_NAME_IGNORE_CONTACTS, COL_TEL_NUMBER + " = ?",
                new String[] { contact.getTelNumber() }) > 0;
    }

    public synchronized boolean updateTargetContact(ContactsData contact) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTACTS_ID, contact.getContactsId());
        values.put(COL_DISPLAYNAME, contact.getDisplayName());
        values.put(COL_TEL_NUMBER, contact.getTelNumber());
        return db.insertOrThrow(TABLE_NAME_IGNORE_CONTACTS, null, values) > 0;
    }

    public synchronized Cursor getContact(ContactsData contact) {
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_IGNORE_CONTACTS + " WHERE " + COL_TEL_NUMBER + " = ?",
                new String[] { contact.getTelNumber() });
    }

    public synchronized Cursor getContacts() {
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_IGNORE_CONTACTS, null);
    }
    
    public synchronized boolean deleteTargetExceptionPrefix (String prefix){
        return db.delete(TABLE_NAME_PREFIX, COL_TEL_NUMBER + " = ?", new String[]{prefix}) > 0;
    }
    
    public synchronized boolean updateTargetExceptionPrefix(String prefix){
        ContentValues values = new ContentValues();
        values.put(COL_TEL_NUMBER, prefix);
        return db.insertOrThrow(TABLE_NAME_PREFIX, null, values) > 0;
    }
    
    public synchronized Cursor getExceptionPrefixs(){
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_PREFIX, null);
    }
}
