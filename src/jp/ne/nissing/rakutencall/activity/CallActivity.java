package jp.ne.nissing.rakutencall.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import jp.ne.nissing.rakutencall.data.ContactsData;
import jp.ne.nissing.rakutencall.data.PhoneActivityData;
import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import jp.ne.nissing.rakutencall.util.DatabaseManager;

public class CallActivity extends Activity {

    private final String TEL = "tel:";
    private final String FREE_DIAL = "0120";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        Uri uri = intent.getData();
        if (uri == null) {
            finish();
            return;
        }

        String telNumber = uri.toString().substring(TEL.length());

        if (needPrefixTel(telNumber) == true) {
            callPhone(TEL + SharedPreferenceManager.getInstance(this).getDefaultPrefixNum()
                    + telNumber);
        } else {
            callPhone(TEL + telNumber);
        }
    }

    /**
     * Prefixの番号をつける必要があるかどうか
     * @param telNum
     * @return true->Prefix番号付与を行う false-> Prefix番号付与を行わない
     */
    private boolean needPrefixTel(String telNum) {
        DatabaseManager db = DatabaseManager.getInstance(this).open();
        Cursor cursor = db.getContact(new ContactsData(telNum, null, null));
        boolean isIgnoreTelNumber = cursor.moveToNext();
        db.close();

        if (telNum.startsWith(SharedPreferenceManager.getInstance(this).getDefaultPrefixNum()) //Prefixナンバーで開始しているかどうか
                || isIgnoreTelNumber   //無視リスト候補の番号かどうか
                || needIgnoreFreedial(telNum)) { //フリーダイアル無視設定でかつフリーダイアルの番号かどうか
            return false;
        }
        return true;
    }
    
    /**
     * フリーダイアルの無視を行うかどうか
     * @param telNum
     * @return true->無視を行う false->無視しない
     */
    private boolean needIgnoreFreedial(String telNum){
        if(SharedPreferenceManager.getInstance(this).getFreeDialCheck() && telNum.startsWith(FREE_DIAL)){
            return true;
        }
        return false;
    }

    private void callPhone(String tel) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(tel));

        PhoneActivityData data = SharedPreferenceManager.getInstance(this).getDefaultPhoneApp();
        if (TextUtils.isEmpty(data.getPackageName()) == false
                && TextUtils.isEmpty(data.getAcitivityName()) == false) {
            intent.setClassName(data.getPackageName(), data.getAcitivityName());
        }

        startActivity(intent);
        finish();
    }
}
