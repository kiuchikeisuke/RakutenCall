package jp.ne.nissing.rakutencall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager {
    private static DatabaseManager mInstance = null;
    
    private static final String DATABASE_NAME = "ignore.db";
    private static final int DATABASE_VERSION = 1;
    
    public static final String TABLE_NAME = "ignore_contacts";
    public static final String COL_TEL_NUMBER = "tel_number";
    public static final String COL_DISPLAYNAME = "display_name";
    public static final String COL_CONTACTS_ID = "contacts_id";
    
    private final Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    
    public static DatabaseManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new DatabaseManager(context);
        }
        return mInstance;
    }
    
    private DatabaseManager(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }
    
    private static class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "";
            sql += "CREATE TABLE " + TABLE_NAME +"(" + 
                    COL_TEL_NUMBER + " TEXT PRIMARY KEY ," +
                    COL_DISPLAYNAME + " TEXT ," + 
                    COL_CONTACTS_ID + " INTEGER " + 
                    ")";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
        
    }
    
    public DatabaseManager open(){
        db = dbHelper.getWritableDatabase();
        return this;
    }
    
    public void close(){
        dbHelper.close();
    }
    
    public boolean deleteAllContacts(){
        return db.delete(TABLE_NAME, null, null) > 0;
    }
    
    public boolean deleteTargetContact(ContactsData contact){
        return db.delete(TABLE_NAME, COL_TEL_NUMBER + " = " + contact.getTelNumber(), null) > 0;
    }
    
    public boolean updateTargetContact(ContactsData contact){
        ContentValues values = new ContentValues();
        values.put(COL_CONTACTS_ID, contact.getContactsId());
        values.put(COL_DISPLAYNAME, contact.getDisplayName());
        values.put(COL_TEL_NUMBER, contact.getTelNumber());
        
        return db.update(TABLE_NAME, values, COL_TEL_NUMBER + " = " + contact.getTelNumber(), null) > 0;
    }
    
    public Cursor getContact(ContactsData contact){
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + "WHERE " + COL_TEL_NUMBER + "=?", new String[]{contact.getTelNumber()});
    }
}
