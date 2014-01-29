package jp.ne.nissing.rakutencall;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private Context mContext;

    // 定数
    private static final int MENU_ID_SETTING = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initIgnoreList();
    }

    private void initIgnoreList() {
        ContentResolver cr = getContentResolver();
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

        // メールアドレスが存在する連絡先だけを名前格納用リストに格納
        List<ContactsData> listItems = new ArrayList<ContactsData>(); // 名前格納用リスト
        while (dataNameTable.moveToNext()) {
            String id = dataNameTable.getString(dataNameTable.getColumnIndex(Data.CONTACT_ID));
            String displayName = dataNameTable.getString(dataNameTable
                    .getColumnIndex(Data.DISPLAY_NAME));

            for (ContactsData item : tempList) {
                if (item.getContactsId().equals(id)) {
                    listItems.add(new ContactsData(item.getTelNumber(), displayName, id));
                }
            }
        }
        // カーソルを閉じる
        dataNameTable.close();

        DatabaseManager db = DatabaseManager.getInstance(this).open();
        Cursor cursor = db.getContacts();
        while (cursor.moveToNext()) {
            String telNum = cursor.getString(cursor.getColumnIndex(DatabaseManager.COL_TEL_NUMBER));
            for (ContactsData contact : listItems) {
                if (contact.getTelNumber().equals(telNum)) {
                    contact.setIgnored(true);
                    break;
                }
            }
        }
        // リスト生成
        ListView ignoreListView = (ListView) findViewById(R.id.ignoreListView);
        mContactsAdapter = new ContactsAdapter(this, 0, listItems);

        ignoreListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("rakuten", position + " @ " + id);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                checkBox.setChecked(!checkBox.isChecked());
                ContactsData data = mContactsAdapter.getItem(position);

                DatabaseManager db = DatabaseManager.getInstance(mContext).open();
                if (checkBox.isChecked()) {
                    db.updateTargetContact(data);
                } else {
                    db.deleteTargetContact(data);
                }
                db.close();
            }
        });
        ignoreListView.setAdapter(mContactsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ID_SETTING, Menu.NONE, R.string.menu_setting);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case MENU_ID_SETTING:
            showPreferenceActivity();
            break;
        default:
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPreferenceActivity() {
        Intent intent = new Intent(getApplicationContext(), PreferenceActivity.class);
        startActivity(intent);
    }

}
