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

    private boolean needPrefixTel(String telNum) {
        DatabaseManager db = DatabaseManager.getInstance(this).open();
        Cursor cursor = db.getContact(new ContactsData(telNum, null, null));
        boolean isIgnoreTelNumber = cursor.moveToNext();
        db.close();

        if (telNum.startsWith(SharedPreferenceManager.getInstance(this).getDefaultPrefixNum())
                || isIgnoreTelNumber) {
            return false;
        }
        return true;
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
