package jp.ne.nissing.rakutencall;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {

    private ContactsAdapter mContactsAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String[] projection = new String[]{Phone.NUMBER,Phone.CONTACT_ID};

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
            }
        });
    }

}
