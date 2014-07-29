package jp.ne.nissing.rakutencall.contacts;

import java.util.List;

import jp.ne.nissing.rakutencall.R;
import jp.ne.nissing.rakutencall.contacts.data.*;
import jp.ne.nissing.rakutencall.db.*;
import jp.ne.nissing.rakutencall.preference.PreferenceActivity;
import android.app.Activity;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class IgnoreContactsSelectionActivity extends Activity {

    private ContactsAdapter mContactsAdapter = null;
    private Context mContext;

    // 定数
    private static final int MENU_ID_SETTING = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ignore_contacts_selection_activity);
        mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initIgnoreList();
    }

    private void initIgnoreList() {

        final ListView ignoreListView = (ListView) findViewById(R.id.ignoreListView);
        final List<ContactsData> listItems = ContactsManager.getInstance(mContext)
                .getIgnoreContactList();
        
        
        ignoreListView.setItemsCanFocus(false);
        ignoreListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ignoreListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                ContactsData data = mContactsAdapter.getItem(position);

                DatabaseManager db = DatabaseManager.getInstance(mContext)
                        .open();
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
                
                //表示上のチェックマークを更新(同一電話番号が複数ある場合への対応)
                for(int i = 0;i < listItems.size();i++){
                    if(data.getTelNumber().equals(listItems.get(i).getTelNumber())){
                        listItems.get(i).setIgnored(!containsDb);
                        ignoreListView.setItemChecked(i, !containsDb);
                    }
                }
            }
        });

        // adapterをセット
        mContactsAdapter = new ContactsAdapter(this, 0, listItems);
        ignoreListView.setAdapter(mContactsAdapter);

        // チェックの状態を反映
        for (int i = 0; i < ignoreListView.getCount(); i++) {
            ignoreListView.setItemChecked(i, listItems.get(i).isIgnored());
        }
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
        Intent intent = new Intent(getApplicationContext(),
                PreferenceActivity.class);
        startActivity(intent);
    }

}
