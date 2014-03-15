package jp.ne.nissing.rakutencall.call;

import jp.ne.nissing.rakutencall.call.condition.*;
import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import jp.ne.nissing.rakutencall.preference.phoneappdata.PhoneActivityData;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

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

    /**
     * Prefixの番号をつける必要があるかどうか
     * @param telNum
     * @return true->Prefix番号付与を行う false-> Prefix番号付与を行わない
     */
    private boolean needPrefixTel(String telNum) {
       AbstractCondition[] conditions = 
           {
               new PrefixEnableSettingCondition(this, telNum),
               new StartWithPrefixNumCondition(this, telNum),
               new IgnoreTelNumCondition(this, telNum),
               new FreeDialCondition(this, telNum),
               };
       return ConditionManager.isPrefixEnable(conditions);
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
