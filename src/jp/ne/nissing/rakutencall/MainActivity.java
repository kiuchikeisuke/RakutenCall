package jp.ne.nissing.rakutencall;

import java.util.*;

import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.*;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	private ContactsAdapter mContactsAdapter = null;
    private Context mContext;
    
    //定数
    private static final String RAKUDEN_PACAGE_NAME = "jp.ne.nissing.rakutencall";
    public static final String MATCH_ASCII = "^[\\u0020-\\u007E]+$";
    
    private static final int MENU_ID_SELECT_PHONE = 1;
    private static final int MENU_ID_PREFIX_NUM = 2;
    
    
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
        ignoreListView.setAdapter(mContactsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(Menu.NONE, MENU_ID_SELECT_PHONE, Menu.NONE, R.string.menu_action_settings);
    	menu.add(Menu.NONE,MENU_ID_PREFIX_NUM, Menu.NONE, R.string.menu_prefix_num_setting);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
        case MENU_ID_SELECT_PHONE:
            showSelectPhoneAppDialog();
            break;
        case MENU_ID_PREFIX_NUM:
            showPrefixPhoneNumDialog();
            break;
        default:
            break;
        }

        return super.onOptionsItemSelected(item);
    }

	private void showPrefixPhoneNumDialog() {
		final EditText editView = new EditText(this);
		editView.setHint(R.string.dialog_prefix_num_hint);
		editView.setText(SharedPreferenceManager.getInstance(this).getDefaultPrefixNum());
		editView.setInputType(InputType.TYPE_CLASS_PHONE);
		editView.selectAll();
		
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.dialog_prefix_num_title);
		alert.setView(editView);
		alert.setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String inputString = editView.getText().toString();
				
				//ASCII文字列範囲内かチェック。範囲外なら反映せず終了
				if(inputString.matches(MATCH_ASCII) == false){
					Toast.makeText(mContext, R.string.dialog_prefix_num_error, Toast.LENGTH_LONG).show();
					dialog.cancel();
					dialog.dismiss();
					return;
				}
				
				//SharedPrefに反映し、トーストを出して終了
				SharedPreferenceManager.getInstance(mContext).setDefaultPrefixNum(inputString);
				Toast.makeText(mContext, getString(R.string.toast_prefix_num_setend, 
						SharedPreferenceManager.getInstance(mContext).getDefaultPrefixNum()), Toast.LENGTH_LONG).show();
				dialog.cancel();
				dialog.dismiss();
			}
		});
		alert.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				dialog.dismiss();
			}
		});
		
		AlertDialog alertDialog = alert.create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.showSoftInput(editView, 0);
			}
		});
		
		alertDialog.show();
	}

	private void showSelectPhoneAppDialog() {
		PackageManager pm = getPackageManager();
		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"));
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		List<ResolveInfo> activities = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		final List<PhoneActivityData> lists = new ArrayList<PhoneActivityData>();
		PhoneActivityData defaultPhoneAppData = SharedPreferenceManager.getInstance(this).getDefaultPhoneApp();
		int defaultPhoneAppIndex = 0;
		            
		for(ResolveInfo act : activities){
		    String label = act.loadLabel(pm).toString();
		    Drawable icon = act.loadIcon(pm);
		    String packageName = act.activityInfo.packageName;
		    String activityName = act.activityInfo.name;

		    //らくでん自身は候補に追加しない
		    if(packageName.equals(RAKUDEN_PACAGE_NAME))
		    	continue;

		    PhoneActivityData data = new PhoneActivityData(label,icon,packageName,activityName);
		    
		    //現在選択中のアプリはチェック済みにする
		    if(packageName.equals(defaultPhoneAppData.getPackageName()) && activityName.equals(defaultPhoneAppData.getAcitivityName()) ){
		    	defaultPhoneAppIndex = lists.size() - 1;
		    	data.setSelected(true);
		    }
		    
		    lists.add(data);
		}

		PhoneActivityDataAdapter adapter = new PhoneActivityDataAdapter(this, 0, lists);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.phone_app_list);
		alert.setSingleChoiceItems(adapter, defaultPhoneAppIndex, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PhoneActivityData phoneActivityData = lists.get(which);
				SharedPreferenceManager.getInstance(mContext).setDefaultPhoneApp(phoneActivityData.getPackageName(),phoneActivityData.getAcitivityName());
				dialog.cancel();
			}
		});
		alert.show();
	}

}
