package jp.ne.nissing.rakutencall;

import java.util.*;

import android.app.Activity;
import android.content.*;
import android.content.pm.*;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

    private ContactsAdapter mContactsAdapter = null;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;


        
        
        
        
        
        ContentResolver cr = getContentResolver();
        Cursor dataAddressTable = cr.query(
                Phone.CONTENT_URI,
                null,
                Data.MIMETYPE + " = ?",
                new String[]{Phone.CONTENT_ITEM_TYPE},
                null,
                null);

        // ハッシュ変数にアドレスを格納(id, address)
        HashMap<String, ContactsData> contactsHash = new HashMap<String, ContactsData>();
        List<ContactsData> tempList = new ArrayList<ContactsData>();
        while(dataAddressTable.moveToNext()) {
            
            String id = dataAddressTable.getString(dataAddressTable.getColumnIndex(Data.CONTACT_ID));
            String telNumber = dataAddressTable.getString(dataAddressTable.getColumnIndex(Data.DATA1));
            ContactsData contactsData = new ContactsData(telNumber, null, id);
            
            contactsHash.put(telNumber,contactsData);
            tempList.add(contactsData);
        }

        // カーソルを閉じる
        dataAddressTable.close();

        // ソート文字を格納（連絡先一覧を "ふりがな" 順でソート）
        String order_str =
                " CASE" +
                        " WHEN IFNULL(" + Data.DATA9 + ", '') = ''" + // Data.DATA9がNULLの場合は空文字を代入
                        " THEN 1 ELSE 0" + // Data.DATA9が空文字のレコードを最後にする
                        " END, " + Data.DATA9 + " ," +
                        " CASE" +
                        " WHEN IFNULL(" + Data.DATA7 + ", '') = ''" +
                        " THEN 1 ELSE 0" +
                        " END, " + Data.DATA7;

        // DATA表から連絡先名を全て取得
        Cursor dataNameTable = cr.query(
                Data.CONTENT_URI,
                null,
                Data.MIMETYPE + " = ?",
                new String[]{StructuredName.CONTENT_ITEM_TYPE},
                order_str);

        // メールアドレスが存在する連絡先だけを名前格納用リストに格納
        List<ContactsData> listItems = new ArrayList<ContactsData>(); // 名前格納用リスト
        while(dataNameTable.moveToNext()) {
            String id = dataNameTable.getString(dataNameTable.getColumnIndex(Data.CONTACT_ID));
            String displayName = dataNameTable.getString(dataNameTable.getColumnIndex(Data.DISPLAY_NAME));
            
            for(ContactsData item :tempList){
                if(item.getContactsId().equals(id)){
                    listItems.add(new ContactsData(item.getTelNumber(), displayName, id));
                }
            }
        }
        // カーソルを閉じる
        dataNameTable.close();

        DatabaseManager db = DatabaseManager.getInstance(this).open();
        Cursor cursor = db.getContacts();
        while(cursor.moveToNext()){
        	String telNum = cursor.getString(cursor.getColumnIndex(DatabaseManager.COL_TEL_NUMBER));
        	for(ContactsData contact : listItems){
        		if(contact.getTelNumber().equals(telNum)){
        			contact.setIgnored(true);
        			break;
        		}
        	}
        }
        
        //リスト生成
        ListView ignoreListView = (ListView) findViewById(R.id.ignoreListView);
        mContactsAdapter = new ContactsAdapter(this,0,listItems);

        ignoreListView.setAdapter(mContactsAdapter);
        ignoreListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("rakuten", position + " @ " + id);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                checkBox.setChecked(!checkBox.isChecked());
                ContactsData data = mContactsAdapter.getItem(position);
                
                DatabaseManager db = DatabaseManager.getInstance(mContext).open();
                if(checkBox.isChecked()){
                    db.updateTargetContact(data);
                }
                else{
                    db.deleteTargetContact(data);
                }
                db.close();
            }
        });
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, R.string.action_settings);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case Menu.FIRST:
	        PackageManager pm = getPackageManager();
	        Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"));
	        intent.addCategory(Intent.CATEGORY_DEFAULT);
	        List<ResolveInfo> activities = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
	        
	        for(ResolveInfo act : activities){
	        	act.loadLabel(pm).toString();
	        	Drawable icon = act.loadIcon(pm);
	        }
	        
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

}
