package jp.ne.nissing.rakutencall.contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;

import jp.ne.nissing.rakutencall.contacts.data.ContactsData;
import jp.ne.nissing.rakutencall.db.DatabaseManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactsRepository {
    private static ContactsRepository instance = null;
    private static Context mContext;
    private List<ContactsData> mContactsList = null;
    
    public ContactsRepository(Context context) {
        mContext = context;
    }

    public static ContactsRepository getInstance(Context context){
        if(instance == null){
            instance = new ContactsRepository(context);
        }
        return instance; 
    }
    
    /**
     * @param executeInit DBからデータを取得するかどうか
     * @return ContactsDataのクローンを取得する
     */
    public List<ContactsData> getIgnoreContactListClone() {
        if(mContactsList == null){
            mContactsList = createIgnoreContactsList(mContext);
        }
        
        List<ContactsData> retval = new ArrayList<ContactsData>();
        for(ContactsData target : mContactsList){
            retval.add(new ContactsData(target));
        }
        return retval;
    }
    
    /**
     * @param data
     * @return 無視リストに追加された場合はtrue,無視リストから削除された場合はfalse
     */
    public boolean update(ContactsData data){
        DatabaseManager db = DatabaseManager.getInstance(mContext).open();
        Cursor cursor = db.getContact(data);
        boolean containsDb = cursor.moveToFirst();
        if(containsDb == false){
            db.updateTargetContact(data);
        } else {
            db.deleteTargetContact(data);
        }
        
        if(db != null){
            db.close();
        }
        if(cursor != null){
            cursor.close();
        }
        
        for(ContactsData target : mContactsList){
            if(data.getTelNumber().equals(target.getTelNumber())){
                target.setIgnored(!containsDb);
            }
        }
        
        return !containsDb;
    }
    
    /**
     * 電話帳データおよびDBから無視リストを生成する
     * @param context
     * @return
     */
    private List<ContactsData> createIgnoreContactsList(Context context){
        ContentResolver cr = context.getContentResolver();
        Cursor dataAddressTable = cr.query(Phone.CONTENT_URI, null, Data.MIMETYPE + " = ?",
                new String[] { Phone.CONTENT_ITEM_TYPE }, null);

        // ハッシュ変数にアドレスを格納(id, address)
        HashMap<String, ContactsData> contactsHash = new HashMap<String, ContactsData>();
        List<ContactsData> tempList = new ArrayList<ContactsData>();
        while (dataAddressTable.moveToNext()) {

            String id = dataAddressTable
                    .getString(dataAddressTable.getColumnIndex(Data.CONTACT_ID));
            String telNumber = dataAddressTable.getString(dataAddressTable
                    .getColumnIndex(Data.DATA1));
            ContactsData contactsData = new ContactsData(telNumber, null, id);

            contactsHash.put(telNumber, contactsData);
            tempList.add(contactsData);
        }

        // カーソルを閉じる
        dataAddressTable.close();

        // ソート文字を格納（連絡先一覧を "ふりがな" 順でソート）
        String order_str = " CASE" + " WHEN IFNULL(" + Data.DATA9 + ", '') = ''"
                + // Data.DATA9がNULLの場合は空文字を代入
                " THEN 1 ELSE 0"
                + // Data.DATA9が空文字のレコードを最後にする
                " END, " + Data.DATA9 + " ," + " CASE" + " WHEN IFNULL(" + Data.DATA7
                + ", '') = ''" + " THEN 1 ELSE 0" + " END, " + Data.DATA7;

        // DATA表から連絡先名を全て取得
        Cursor dataNameTable = cr.query(Data.CONTENT_URI, null, Data.MIMETYPE + " = ?",
                new String[] { StructuredName.CONTENT_ITEM_TYPE }, order_str);

        // 電話番号が存在する連絡先だけを名前格納用リストに格納
        List<ContactsData> retval = new ArrayList<ContactsData>(); // 名前格納用リスト
        while (dataNameTable.moveToNext()) {
            String id = dataNameTable.getString(dataNameTable.getColumnIndex(Data.CONTACT_ID));
            String displayName = dataNameTable.getString(dataNameTable
                    .getColumnIndex(Data.DISPLAY_NAME));

            for (ContactsData item : tempList) {
                if (item.getContactsId().equals(id)) {
                    retval.add(new ContactsData(item.getTelNumber(), displayName, id));
                }
            }
        }
        // カーソルを閉じる
        dataNameTable.close();

        DatabaseManager db = DatabaseManager.getInstance(context).open();
        Cursor cursor = db.getContacts();
        
        List<String> ignoreNumList = new ArrayList<String>();
        while(cursor.moveToNext()){
            ignoreNumList.add(cursor.getString(cursor.getColumnIndex(DatabaseManager.COL_TEL_NUMBER)));
        }
        
        for(ContactsData contact : retval){
            if(ignoreNumList.contains(contact.getTelNumber())){
                contact.setIgnored(true);
            }
        }
        if(db != null){
            db.close();
        }
        if(cursor != null){
            cursor.close();
        }
        
        return retval;
    }

}
